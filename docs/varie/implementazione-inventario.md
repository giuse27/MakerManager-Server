# Implementazione dell'Inventario 

> [!note] 
> Questo file è stato generato da Claude Sonnet 5 (impegno medio, senza pensiero adattivo)
> Va inteso come un file di riepilogo e brainstorming
> Attualmente sono presenti lievi disallineamenti tra questo file e il progetto

Questo documento spiega come implementare la gestione dell'Inventario nel Server di MakerManager: come si struttura il dominio (`Inventario` → `ArticoloInventario` → `ElementoCatalogo`), come organizzare i package secondo le convenzioni Spring, e quali endpoint REST esporre nella v0.0.0.

Il documento è diviso in due parti:
- **Paragrafo 1** — cosa implementare ora, nella v0.0.0 (architettura base, roadmap "Server → Architettura base").
- **Paragrafo 2** — come evolverà in versioni future (introduzione di una vera entità `Utente`).

---

## Premessa: le convenzioni dei package in Spring Boot

Prima di entrare nel dettaglio, vale la pena spiegare *perché* i file vanno in un certo package, dato che è la tua prima volta con Spring.

Un progetto Spring Boot si organizza tipicamente per **layer** (strati), non per feature. Ogni layer ha una responsabilità precisa (è lo stesso principio del Single Responsibility Principle applicato all'architettura):

| Package | Responsabilità | Esempio |
|---|---|---|
| `model` | Rappresenta i dati e le regole intrinseche dell'oggetto (entità JPA) | `Inventario`, `ArticoloInventario` |
| `repository` | Accesso al database, query. Nessuna logica di business | `InventarioRepository` |
| `service` | Logica di business: orchestrazione, validazioni, decisioni | `InventarioService` |
| `controller` | Espone gli endpoint HTTP, riceve richieste e restituisce risposte | `InventarioController` |
| `dto` | Oggetti "leggeri" usati per lo scambio dati via JSON, separati dalle entità JPA | `NuovoArticoloRequest` |

Questa separazione (nota come **architettura a livelli**, *layered architecture*) è la convenzione più diffusa in Spring perché:
1. **Isola le responsabilità**: il controller non sa nulla di JPA, il repository non sa nulla di HTTP.
2. **Facilita i test**: puoi testare il `service` senza avviare un server HTTP.
3. **Rende il codice prevedibile**: chiunque guardi il progetto sa dove cercare cosa.

La regola pratica è: **le richieste attraversano i layer dall'alto verso il basso** — `Controller → Service → Repository → Database` — e mai al contrario (un `repository` non dovrebbe mai chiamare un `service`).

---

## Paragrafo 1 — v0.0.0: architettura base dell'Inventario

### Inquadramento nella roadmap

Guardando `structure.md` e la roadmap in `README.md`, la v0.0.0 lato Server prevede:
- `model` → inventario ✅ (già presente)
- `repository` ✅ (già presente)
- `controller` → ⬜ (prossimo step, oggetto di questo paragrafo)

Questo documento quindi completa la parte mancante della v0.0.0: gli **endpoint HTTP di base** per creare un inventario e gestirne gli articoli, mantenendo la UI minimale già prevista (nessuna feature di "progetti consigliati" o simili, che sono roba da v2.0.0).

### 1. Il dominio: come si relazionano le entità

```
Inventario (1) ──< (N) ArticoloInventario >── (1) ElementoCatalogo
     |
     idUtente : Long   (nessuna relazione JPA nella v0.0.0)
```

- **`Inventario`**: contenitore di `ArticoloInventario`, associato a un utente tramite un semplice `Long idUtente`.
- **`ArticoloInventario`**: classe astratta (pattern Type-Instance, `SINGLE_TABLE`), rappresenta il possesso fisico di un `ElementoCatalogo`. Le sottoclassi (`ComponenteElettronico`, `MaterialeConsumabile`, `StrumentoDiLavoro`) esistono già nel model.
- **`ElementoCatalogo`**: il "modello teorico" dell'oggetto (es. "Raspberry Pi 4"), con la sua `TipologiaElemento`. Viene popolato una volta sola tramite `/inizializza` (vedi `Contesto e requisiti`).

Questo è già tutto presente e corretto nel tuo `model` e `repository` attuali — non serve modificarli per la v0.0.0. Il lavoro da fare è nel `service` e nel `controller`.

> 💡 **Osservazione (best practice JPA)**: `Inventario.articoli` usa già `mappedBy`, `cascade = CascadeType.ALL` e `orphanRemoval = true`. Questa è la scelta corretta per una relazione "possesso esclusivo" (un `ArticoloInventario` non ha senso senza il suo `Inventario`): quando elimini un `Inventario`, Hibernate elimina automaticamente anche i suoi articoli, e se rimuovi un articolo dalla lista Java, Hibernate lo cancella anche dal DB. Non serve altro qui.

### 2. Il flusso di creazione di un articolo (come deciso insieme)

Il client non sceglie la sottoclasse Java da istanziare: passa solo l'id dell'`ElementoCatalogo` (che porta già con sé la `TipologiaElemento`) e la quantità. È il **server** a decidere quale sottoclasse concreta creare, leggendo la tipologia.

```
Client:  POST /api/inventari/5/articoli
         { "elementoCatalogoId": 12, "quantita": 3 }

Server:  1. Trova l'ElementoCatalogo con id 12
         2. Legge la sua TipologiaElemento (es. COMPONENTE_ELETTRONICO)
         3. Istanzia la sottoclasse corrispondente (ComponenteElettronico)
         4. La aggiunge all'Inventario 5 e salva
```

Questa decisione va centralizzata in un unico punto per rispettare il SRP: nessun altro layer dovrebbe contenere questo `if/switch`. Il posto corretto **non** è il controller (che deve restare "sottile": riceve/valida input HTTP, delega, restituisce output), **non** è il repository (che fa solo query), e — come deciso di seguito — nemmeno un metodo privato dentro il service: merita una classe propria.

### 2.1 La `ArticoloInventarioFactory`

Hai confermato che oggi `InizializzazioneService` contiene già un `if/switch` che, leggendo la `TipologiaElemento`, istanzia la sottoclasse concreta corretta di `ArticoloInventario` (`ComponenteElettronico`, `MaterialeConsumabile`, `StrumentoDiLavoro`). Questa è **esattamente** la stessa decisione che dovrà prendere `InventarioService` quando aggiunge un articolo via `POST /api/inventari/{id}/articoli`.

Avere questa logica duplicata in due punti diversi (`InizializzazioneService` e, in futuro, `InventarioService`) violerebbe sia il SRP sia il principio DRY (*Don't Repeat Yourself*): se domani aggiungi una quarta sottoclasse di `ArticoloInventario`, dovresti ricordarti di aggiornare l'`if/switch` in due posti — è un rischio concreto di bug (ne dimentichi uno e il comportamento tra le due strade diventa incoerente).

La soluzione è estrarre questa logica in una **classe Factory dedicata**, che sia l'unico punto del sistema autorizzato a decidere "quale sottoclasse istanziare per questa tipologia":

```
src/main/java/it/unipi/makermanagerserver
│
└── 📁 factory
    └── ArticoloInventarioFactory.java
```

**Perché un package `factory` a parte, e non dentro `model` o `service`?**
- Non va in `model`: il `model` contiene solo le entità JPA (dati + regole intrinseche dell'oggetto), non la logica su *come* costruirle a partire da altri dati.
- Non va in `service`: un service orchestra un caso d'uso completo (validazioni, chiamate a repository, gestione di errori HTTP-friendly); la factory invece ha una responsabilità molto più stretta e riutilizzabile — "dato un tipo, costruiscimi l'oggetto giusto" — e viene *usata da* più service (sia `InventarioService` che `InizializzazioneService`).

Questo è un caso da manuale del **pattern Factory** (in questo caso più precisamente una *Simple Factory*, non il *Factory Method* col polimorfismo su una gerarchia di factory): una classe con un metodo che, dato un parametro discriminante (`TipologiaElemento`), restituisce l'istanza concreta corretta. Tipicamente si scrive come classe con metodo statico, dato che non ha stato proprio da mantenere:

```java
// Idea concettuale, non ancora codice da implementare:
ArticoloInventarioFactory.crea(tipologia, elementoCatalogo, inventario, quantita)
   → restituisce un ComponenteElettronico, MaterialeConsumabile o StrumentoDiLavoro
```

**Effetto sul codice esistente**: una volta introdotta, sia `InizializzazioneService` sia (il futuro) `InventarioService` chiameranno questa factory invece di avere ciascuno il proprio `if/switch`. Il refactoring di `InizializzazioneService` per usarla è quindi un piccolo task di pulizia da fare in questo stesso step, contestualmente alla creazione della factory — non va rimandato, perché altrimenti per un periodo avresti due implementazioni della stessa logica in giro (una nella factory, una ancora dentro `InizializzazioneService`), che è proprio la situazione che vogliamo evitare.

> 💡 **Nota Spring/JPA**: la factory *non* deve estendere `JpaRepository` né avere annotazioni come `@Service` o `@Component` a meno che tu non voglia iniettarla via dependency injection in altri bean (cosa che probabilmente vorrai fare, dato che sia `InventarioService` che `InizializzazioneService` dovranno usarla). In tal caso sì, andrà annotata con `@Component` (o `@Service`, semanticamente meno corretto qui dato che non è un "servizio applicativo" ma una utility) così Spring la gestisce come singleton e te la inietta col costruttore, seguendo la convenzione *constructor injection* che è la best practice raccomandata in Spring (preferibile a `@Autowired` su campo, perché rende le dipendenze esplicite ed è più facile da testare).

### 3. Struttura dei file da creare (v0.0.0)

Ecco cosa manca e dove va posizionato, seguendo `structure.md`:

```
src/main/java/it/unipi/makermanagerserver
│
├── 📁 dto
│   ├── NuovoInventarioRequest.java     <-- input di POST /api/inventari
│   ├── NuovoArticoloRequest.java       <-- input di POST /api/inventari/{id}/articoli
│   └── InventarioResponse.java         <-- output "sicuro" verso il client (evita di esporre l'entità JPA)
│
├── 📁 service
│   └── InventarioService.java          <-- logica: crea inventario, aggiungi/rimuovi articolo
│
├── 📁 controller
│   └── InventarioController.java       <-- espone gli endpoint su /api/inventari
│
└── 📁 factory
    └── ArticoloInventarioFactory.java  <-- unico punto che decide quale sottoclasse istanziare
                                            (sostituisce l'if/switch oggi in InizializzazioneService)
```

**Perché un package `dto` separato dal `model`?**
Le entità JPA (`Inventario`, `ArticoloInventario`) sono pensate per Hibernate: hanno relazioni bidirezionali, lazy loading, e se le restituisci direttamente come risposta JSON rischi:
- **loop infiniti di serializzazione** (`Inventario` → `articoli` → ognuno ha un riferimento a `inventario` → di nuovo `Inventario`...);
- di esporre dettagli interni che il client non deve vedere.

Per questo è convenzione avere DTO (*Data Transfer Object*) distinti: classi semplici, senza annotazioni JPA, usate solo per il traffico in entrata/uscita via HTTP. Il `service` (o un piccolo `mapper`) si occupa di convertire Entità ↔ DTO.

> 💡 Questo è un ottimo esempio di dove applicare il tuo pattern preferito: potresti creare un package `mapper` con classi tipo `InventarioMapper` che centralizzano la conversione Entità→DTO, invece di scrivere quella logica dentro il service (che altrimenti farebbe "troppe cose", violando SRP). Fammi sapere se vuoi che lo includa già in questo step o se preferisci rimandarlo a quando il numero di DTO cresce.

### 4. Endpoint REST proposti (v0.0.0)

Come concordato, il set minimo:

| Metodo | Endpoint | Scopo |
|---|---|---|
| `POST` | `/api/inventari` | Crea un nuovo inventario, associato a un `idUtente` |
| `GET` | `/api/inventari/{id}` | Recupera un inventario con i suoi articoli |
| `POST` | `/api/inventari/{id}/articoli` | Aggiunge un articolo (`elementoCatalogoId` + `quantita`) |
| `DELETE` | `/api/inventari/{id}/articoli/{articoloId}` | Rimuove un articolo dall'inventario |

Questi endpoint coprono esattamente il requisito "lista con ContextMenu per l'eliminazione" richiesto nel documento di contesto, lato client: il client farà `GET` per popolare la lista e `DELETE` per l'eliminazione da menu contestuale.

> 💡 **Nota su REST**: l'annidamento `/api/inventari/{id}/articoli/{articoloId}` è la convenzione REST corretta per esprimere "l'articolo appartiene all'inventario" — evita endpoint "piatti" tipo `/api/articoli/{id}` che perderebbero questa relazione gerarchica nell'URL.

### 5. Cosa NON fare ancora (per restare aderenti alla roadmap)

Per non anticipare step futuri della roadmap (`v1.0.0` prevede "creazione di un nuovo inventario" come funzione applicativa completa, con UI):
- **Non** implementare ancora `PUT /api/inventari/{id}/articoli/{articoloId}` per modificare la quantità: la roadmap v0.0.0 parla di "architettura base", l'update è una raffinatura successiva.
- **Non** aggiungere ancora validazioni complesse (es. controllo se l'utente esiste davvero) dato che l'entità `Utente` non esiste ancora.
- **Non** introdurre ancora sicurezza/autenticazione: `idUtente` resta un dato "fidato" passato dal client.

### 6. Nota per il prossimo step: gestione del `ElementoCatalogo` (fuori scope da questo step)

Oggi il catalogo (`ElementoCatalogo`) si popola solo in blocco tramite `/inizializza`, leggendo un JSON. Serviranno però anche endpoint dedicati per **aggiungere o rimuovere singoli elementi di catalogo** "a mano" (es. un maker che vuole registrare un nuovo tipo di componente non presente nel JSON iniziale).

Questa è una funzionalità concettualmente **separata** dall'Inventario: il Catalogo è il modello teorico condiviso (`ElementoCatalogo`), l'Inventario è il possesso fisico personale (`ArticoloInventario`). Per rispettare il SRP, non va mescolata nello stesso controller:

```
📁 controller
├── InventarioController.java   <-- /api/inventari/...
└── CatalogoController.java     <-- /api/catalogo/...   (step successivo)
```

Coerentemente con la roadmap — che nella v0.0.0 elenca solo "architettura base" per `model` e `repository`, con `controller` ancora da completare — questo `CatalogoController` **non fa parte di questo step**: lo affronteremo come attività a sé stante una volta chiuso l'Inventario, per non accumulare troppe funzionalità in un colpo solo. Lo segno qui solo come promemoria, in modo che quando arriveremo a scrivere `InizializzazioneService`/factory non perdiamo di vista che il catalogo avrà bisogno di CRUD dedicato in futuro.

---

## Paragrafo 2 — Versioni future: introduzione dell'entità `Utente`

Quando (in una versione successiva, presumibilmente v1.0.0 o oltre, quando emergerà il bisogno di gestione utenti multipli) vorrai trasformare `idUtente` in una vera relazione JPA, i cambiamenti saranno:

### Nel model

```java
@Entity
@Table(name = "utenti")
public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    // ... altri campi (email, credenziali, ecc. secondo necessità)

    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inventario> inventari = new ArrayList<>();
}
```

E in `Inventario`, il campo scalare verrà sostituito da una relazione `@ManyToOne`:

```java
@ManyToOne
@JoinColumn(name = "utente_id")
private Utente utente;
```

Questo va posizionato in un nuovo package `model.user` (parallelo a `model.catalog`, `model.inventory`, `model.project`), per restare coerenti con la struttura a package-per-dominio già in uso.

### Impatti a cascata

Questo cambiamento non è "gratuito": introdurrà bisogni che nella v0.0.0 non esistono ancora, come ad esempio:
- un `UtenteRepository` e (probabilmente) un `UtenteService`/`UtenteController` per la gestione base degli utenti;
- la validazione, in `InventarioService`, che l'`Utente` referenziato esista davvero prima di creare un `Inventario` (oggi impossibile, perché non c'è una tabella `utenti` da controllare);
- eventualmente un sistema di autenticazione, se il progetto arriverà a gestire più utenti "reali" e non solo un placeholder.

Per questo motivo è corretto **non** anticipare questa struttura adesso: aggiungerla oggi introdurrebbe complessità (gestione di una entità che nessun altro componente del sistema usa ancora) senza alcun beneficio nello stato attuale del progetto.

---

## Prossimi passi

Decisioni prese finora:
- ✅ Factory dedicata (`ArticoloInventarioFactory`) in package `factory`, con contestuale refactoring di `InizializzazioneService` per usarla al posto del proprio `if/switch`.
- ✅ `CatalogoController` separato da `InventarioController`, ma **rimandato** a uno step successivo alla chiusura dell'Inventario.

Per lo step attuale (v0.0.0 — Inventario), l'ordine di lavoro suggerito è:
1. `ArticoloInventarioFactory` (sblocca sia il refactoring di `InizializzazioneService` sia il futuro `InventarioService`).
2. Refactoring di `InizializzazioneService` per usare la factory.
3. DTO (`NuovoInventarioRequest`, `NuovoArticoloRequest`, `InventarioResponse`).
4. `InventarioService`.
5. `InventarioController`.

Procederò un pezzo alla volta, fermandomi a chiedere conferma prima di ogni passaggio se emergono bivi progettuali — ricordando che, come da tua indicazione, in questa fase produco solo **documentazione**, non codice, a meno che tu non mi chieda esplicitamente di scriverlo.