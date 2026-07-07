# Endpoint esposti dal Server

- [Endpoint esposti dal Server](#endpoint-esposti-dal-server)
  - [Autenticazione e permessi](#autenticazione-e-permessi)
  - [Inizializzazione](#inizializzazione)
    - [Inizializza il database (ADMIN)](#inizializza-il-database-admin)
  - [Autenticazione](#autenticazione)
    - [Registrazione (PUBBLICO)](#registrazione-pubblico)
    - [Login (PUBBLICO)](#login-pubblico)
  - [Utenti](#utenti)
    - [Visualizza il proprio profilo (AUTENTICATO)](#visualizza-il-proprio-profilo-autenticato)
    - [Visualizza tutti gli utenti (ADMIN)](#visualizza-tutti-gli-utenti-admin)
  - [Catalogo](#catalogo)
    - [Visualizza elementi in catalogo (PUBBLICO)](#visualizza-elementi-in-catalogo-pubblico)
    - [Aggiungi un elemento al catalogo (AUTENTICATO)](#aggiungi-un-elemento-al-catalogo-autenticato)
    - [Elimina un elemento dal catalogo a partire dal suo `id` (ADMIN)](#elimina-un-elemento-dal-catalogo-a-partire-dal-suo-id-admin)
  - [Inventario](#inventario)
    - [Visualizza tutti gli inventari (ADMIN)](#visualizza-tutti-gli-inventari-admin)
    - [Visualizza il contenuto di un inventario (PROPRIETARIO / ADMIN)](#visualizza-il-contenuto-di-un-inventario-proprietario--admin)
    - [Visualizza gli inventari di un utente (PROPRIETARIO / ADMIN)](#visualizza-gli-inventari-di-un-utente-proprietario--admin)
    - [Crea un nuovo inventario (AUTENTICATO)](#crea-un-nuovo-inventario-autenticato)
    - [Elimina un inventario a partire dal suo `id` (PROPRIETARIO / ADMIN)](#elimina-un-inventario-a-partire-dal-suo-id-proprietario--admin)
    - [Aggiungi un articolo a un inventario (PROPRIETARIO / ADMIN)](#aggiungi-un-articolo-a-un-inventario-proprietario--admin)
    - [Elimina un articolo dall'inventario a partire dal suo `id` (PROPRIETARIO / ADMIN)](#elimina-un-articolo-dallinventario-a-partire-dal-suo-id-proprietario--admin)
  - [Progetti](#progetti)
    - [Visualizza tutti i progetti (PUBBLICO)](#visualizza-tutti-i-progetti-pubblico)
    - [Visualizza un progetto a partire dal suo `id` (PUBBLICO)](#visualizza-un-progetto-a-partire-dal-suo-id-pubblico)
    - [Visualizza i progetti di una tipologia (PUBBLICO)](#visualizza-i-progetti-di-una-tipologia-pubblico)
    - [Visualizza i progetti di un utente (PUBBLICO)](#visualizza-i-progetti-di-un-utente-pubblico)
    - [Crea un nuovo progetto (AUTENTICATO)](#crea-un-nuovo-progetto-autenticato)
    - [Elimina un progetto a partire dal suo `id` (PROPRIETARIO / ADMIN)](#elimina-un-progetto-a-partire-dal-suo-id-proprietario--admin)
    - [Aggiungi una riga alla BOM di un progetto (PROPRIETARIO / ADMIN)](#aggiungi-una-riga-alla-bom-di-un-progetto-proprietario--admin)
    - [Elimina una riga dalla BOM di un progetto a partire dal suo `id` (PROPRIETARIO / ADMIN)](#elimina-una-riga-dalla-bom-di-un-progetto-a-partire-dal-suo-id-proprietario--admin)

## Autenticazione e permessi

A partire da questa versione il server richiede autenticazione tramite **JWT** per la maggior parte degli endpoint. Il flusso è:

1. Registrati con `POST /auth/registrazione` oppure autenticati con `POST /auth/login`: entrambi restituiscono un `token`.
2. Aggiungi l'header `Authorization: Bearer <token>` a ogni richiesta successiva che lo richieda.

Ogni utente ha un `ruolo`: `UTENTE` (assegnato automaticamente in fase di registrazione) oppure `ADMIN` (assegnato solo manualmente; il server crea comunque sempre un ADMIN di default al primo avvio, se non ne esiste già uno — vedi `AdminBootstrap` e le proprietà `admin.default.*` in `application.properties`).

Per ogni endpoint qui sotto è indicato chi può raggiungerlo:

- **PUBBLICO**: chiunque, anche senza token.
- **AUTENTICATO**: chiunque abbia fatto login (UTENTE o ADMIN).
- **ADMIN**: solo utenti con ruolo ADMIN.
- **Proprietario o ADMIN**: oltre al ruolo richiesto sull'endpoint, il Service verifica anche che la risorsa (inventario, progetto, articolo, riga BOM) appartenga proprio all'utente autenticato, a meno che non sia ADMIN. In caso contrario risponde `403 Forbidden`.

Le regole complete (che possono cambiare senza bisogno di ricompilare o riavviare il server) sono nel file `config/permessi-endpoint.properties`.

## Inizializzazione

### Inizializza il database (ADMIN)

```js
POST http://localhost:8080/inizializza
```

**Permessi:** ADMIN.

Permette di inizializzare il database cancellando il suo contenuto (eccetto l'utente ADMIN di default) e popolandolo con gli elementi, gli inventari, gli utenti, i progetti ecc. contenuti nel file di inizializzazione JSON, posizionato in `src/main/resources/data/inizializzazione.json`.

**Esempio da terminale:**
```bash
curl -v -X POST http://localhost:8080/inizializza \
     -H "Authorization: Bearer {{token}}"
```

> N.B. il `-v` non è obbligatorio, serve solo per mostrare l'output completo del server.

## Autenticazione

### Registrazione (PUBBLICO)

```js
POST http://localhost:8080/auth/registrazione
```

**Permessi:** PUBBLICO.

Registra un nuovo utente con ruolo `UTENTE` e restituisce subito un token JWT valido: non serve un login separato dopo la registrazione. Risponde con `201 Created`, `409 Conflict` se email o nickname sono già in uso, `400 Bad Request` se i dati non rispettano i vincoli (`nickname` obbligatorio, `email` obbligatoria e valida, `password` di almeno 8 caratteri).

**Esempio da terminale:**
```bash
curl -X POST http://localhost:8080/auth/registrazione \
     -H "Content-Type: application/json" \
     -d '{
           "nickname": "marioR",
           "email": "mario.rossi@test.com",
           "password": "password123"
         }'
```

### Login (PUBBLICO)

```js
POST http://localhost:8080/auth/login
```

**Permessi:** PUBBLICO.

Verifica le credenziali fornite e restituisce un token JWT valido. Risponde con `401 Unauthorized` (messaggio generico, senza specificare se è sbagliata l'email o la password) se le credenziali non sono corrette.

**Esempio da terminale:**
```bash
curl -X POST http://localhost:8080/auth/login \
     -H "Content-Type: application/json" \
     -d '{
           "email": "mario.rossi@test.com",
           "password": "password123"
         }'
```

## Utenti

### Visualizza il proprio profilo (AUTENTICATO)

```js
GET http://localhost:8080/api/utenti/me
```

**Permessi:** AUTENTICATO. Restituisce sempre e solo il profilo di chi ha effettuato la richiesta (ricavato dal token, non da un parametro).

**Esempio da terminale:**
```bash
curl -v http://localhost:8080/api/utenti/me \
     -H "Authorization: Bearer {{token}}"
```

### Visualizza tutti gli utenti (ADMIN)

```js
GET http://localhost:8080/api/utenti
```

**Permessi:** ADMIN.

Restituisce l'elenco di tutti gli utenti registrati.

**Esempio da terminale:**
```bash
curl -v http://localhost:8080/api/utenti \
     -H "Authorization: Bearer {{tokenAdmin}}"
```

## Catalogo

### Visualizza elementi in catalogo (PUBBLICO)

```js
GET http://localhost:8080/api/catalogo
```

**Permessi:** PUBBLICO.

Restituisce la lista completa di elementi presenti in catalogo.

**Esempio da terminale:**
```bash
curl -v -X GET http://localhost:8080/api/catalogo
```

### Aggiungi un elemento al catalogo (AUTENTICATO)

```js
POST http://localhost:8080/api/catalogo
```

**Permessi:** AUTENTICATO. Chiunque abbia fatto login può contribuire al catalogo condiviso; solo l'eliminazione è riservata ad ADMIN (vedi endpoint successivo).

Permette la creazione e l'inserimento di un elemento in catalogo dalle caratteristiche specificate.

**Esempio da terminale:**
```bash
curl -X POST http://localhost:8080/api/catalogo \
     -H "Authorization: Bearer {{token}}" \
     -H "Content-Type: application/json" \
     -d '{
           "nome": "Elemento Test Eliminazione",
           "descrizione": "Creato solo per essere cancellato",
           "tipologia": "COMPONENTE_ELETTRONICO"
         }'
```

### Elimina un elemento dal catalogo a partire dal suo `id` (ADMIN)

> [!note]
> L'operazione di eliminazione di un elemento dal catalogo è possibile solo se l'id di quell'elemento non funge da chiave esterna in nessun'altra tabella. Quindi un elemento può essere rimosso dal catalogo se e solo se nessun utente lo possiede nell'inventario e nessun progetto lo contiene nella sua B.O.M. Se referenziato, oggi il server risponde con `500 Internal Server Error` "grezzo" (il vincolo di integrità referenziale di MySQL risale non intercettato fino al `GlobalExceptionHandler` generico).

```js
DELETE http://localhost:8080/api/catalogo/{id}
```

**Permessi:** ADMIN.

Consente l'eliminazione di un elemento, a partire dal suo id, dal catalogo a patto che le condizioni descritte prima siano verificate. Bisogna solo sostituire `{id}` con l'id dell'elemento che si vuole rimuovere dal catalogo.

**Esempio da terminale:**
```bash
curl -v -X DELETE http://localhost:8080/api/catalogo/{id} \
     -H "Authorization: Bearer {{tokenAdmin}}"
```

## Inventario

Un inventario è un dato **personale**: solo il proprietario o un ADMIN possono consultarlo o modificarlo. Il proprietario non è mai indicato dal client (né nel body né nel path): viene sempre ricavato dal token JWT di chi effettua la richiesta.

### Visualizza tutti gli inventari (ADMIN)

```js
GET http://localhost:8080/api/inventario
```

**Permessi:** ADMIN.

Restituisce l'elenco di tutti gli inventari presenti nel database.

**Esempio da terminale:**
```bash
curl -v -X GET http://localhost:8080/api/inventario \
     -H "Authorization: Bearer {{tokenAdmin}}"
```

### Visualizza il contenuto di un inventario (PROPRIETARIO / ADMIN)

```js
GET http://localhost:8080/api/inventario/{idInventario}
```

**Permessi:** Proprietario o ADMIN.

Restituisce la lista degli articoli contenuti nell'inventario indicato da `{idInventario}`. Risponde con `403 Forbidden` se l'inventario non appartiene a chi effettua la richiesta, `404 Not Found` se l'id non esiste.

**Esempio da terminale:**
```bash
curl -v -X GET http://localhost:8080/api/inventario/{idInventario} \
     -H "Authorization: Bearer {{token}}"
```

### Visualizza gli inventari di un utente (PROPRIETARIO / ADMIN)

```js
GET http://localhost:8080/api/inventario/utente/{idUtente}
```

**Permessi:** Se stesso o ADMIN.

Restituisce l'elenco degli inventari associati all'utente indicato da `{idUtente}`. Risponde con `403 Forbidden` se `{idUtente}` non corrisponde a chi effettua la richiesta.

**Esempio da terminale:**
```bash
curl -v -X GET http://localhost:8080/api/inventario/utente/{idUtente} \
     -H "Authorization: Bearer {{token}}"
```

### Crea un nuovo inventario (AUTENTICATO)

```js
POST http://localhost:8080/api/inventario
```

**Permessi:** AUTENTICATO. Il proprietario è sempre l'utente autenticato che effettua la richiesta: il body contiene solo il `nome`, non un `idUtente` scelto dal client.

**Esempio da terminale:**
```bash
curl -X POST http://localhost:8080/api/inventario \
     -H "Authorization: Bearer {{token}}" \
     -H "Content-Type: application/json" \
     -d '{
           "nome": "Il mio primo inventario"
         }'
```

### Elimina un inventario a partire dal suo `id` (PROPRIETARIO / ADMIN)

> [!note]
> L'eliminazione di un inventario è distruttiva: cancella automaticamente anche tutti gli articoli in esso contenuti (relazione `Inventario` → `ArticoloInventario` con `cascade = CascadeType.ALL` e `orphanRemoval = true`). Non richiede la rimozione preventiva dei singoli articoli.

```js
DELETE http://localhost:8080/api/inventario/{idInventario}
```

**Permessi:** Proprietario o ADMIN.

Consente l'eliminazione dell'inventario indicato da `{idInventario}`, insieme a tutti gli articoli che contiene.

**Esempio da terminale:**
```bash
curl -v -X DELETE http://localhost:8080/api/inventario/{idInventario} \
     -H "Authorization: Bearer {{token}}"
```

### Aggiungi un articolo a un inventario (PROPRIETARIO / ADMIN)

> [!note]
> A differenza degli altri endpoint di questa sezione, l'inventario di destinazione non viene indicato nell'URL ma nel body della richiesta (campo `idInventario`), perché un `ArticoloInventario` possiede già un proprio `id` univoco indipendente dall'inventario che lo contiene.

```js
POST http://localhost:8080/api/inventario/articoli
```

**Permessi:** Proprietario dell'inventario di destinazione o ADMIN.

Permette la creazione di un nuovo articolo in inventario, specificando l'elemento di catalogo di riferimento, l'inventario di destinazione e la quantità.

**Esempio da terminale:**
```bash
curl -X POST http://localhost:8080/api/inventario/articoli \
     -H "Authorization: Bearer {{token}}" \
     -H "Content-Type: application/json" \
     -d '{
           "idElementoCatalogo": 1,
           "idInventario": 1,
           "quantita": 2
         }'
```

### Elimina un articolo dall'inventario a partire dal suo `id` (PROPRIETARIO / ADMIN)

```js
DELETE http://localhost:8080/api/inventario/articoli/{idArticolo}
```

**Permessi:** Proprietario dell'inventario a cui appartiene l'articolo, o ADMIN.

Consente l'eliminazione dell'articolo indicato da `{idArticolo}`. Non è necessario indicare l'inventario di appartenenza, poiché l'id dell'articolo è già una chiave univoca in tutto il database.

**Esempio da terminale:**
```bash
curl -v -X DELETE http://localhost:8080/api/inventario/articoli/{idArticolo} \
     -H "Authorization: Bearer {{token}}"
```

## Progetti

I progetti compongono un catalogo **condiviso**: la lettura resta pubblica per chiunque (a differenza degli inventari). Solo le operazioni che modificano un progetto (creazione, eliminazione, modifica della BOM) richiedono autenticazione e sono legate alla proprietà: un progetto appartiene sempre a chi lo ha creato, e solo lui (o un ADMIN) può modificarlo o eliminarlo.

### Visualizza tutti i progetti (PUBBLICO)

```js
GET http://localhost:8080/api/progetti
```

**Permessi:** PUBBLICO.

Restituisce la lista completa dei progetti presenti in catalogo (vista sintetica, senza BOM).

**Esempio da terminale:**
```bash
curl -v -X GET http://localhost:8080/api/progetti
```

### Visualizza un progetto a partire dal suo `id` (PUBBLICO)

> [!note]
> A differenza di `GET /api/progetti` (vista sintetica, senza BOM), questo endpoint restituisce tutti i dettagli del progetto inclusa la sua distinta base (`bom`), con l'elenco delle righe che la compongono.

```js
GET http://localhost:8080/api/progetti/{idProgetto}
```

**Permessi:** PUBBLICO.

Restituisce il progetto indicato da `{idProgetto}`, comprensivo della sua B.O.M. Risponde con 404 Not Found se l'id non corrisponde a nessun progetto esistente.

**Esempio da terminale:**
```bash
curl -v -X GET http://localhost:8080/api/progetti/{idProgetto}
```

### Visualizza i progetti di una tipologia (PUBBLICO)

```js
GET http://localhost:8080/api/progetti/tipologia/{tipologia}
```

**Permessi:** PUBBLICO.

Restituisce l'elenco dei progetti appartenenti alla tipologia indicata (`STAMPA_3D`, `ELETTRONICA`, `ROBOTICA`, `SOFTWARE`). Risponde con `400 Bad Request` se la tipologia non è tra i valori ammessi.

**Esempio da terminale:**
```bash
curl -v -X GET http://localhost:8080/api/progetti/tipologia/STAMPA_3D
```

### Visualizza i progetti di un utente (PUBBLICO)

```js
GET http://localhost:8080/api/progetti/utente/{idUtente}
```

**Permessi:** PUBBLICO. Lettura pubblica: i progetti compongono un catalogo condiviso, non un dato personale (a differenza degli inventari).

**Esempio da terminale:**
```bash
curl -v -X GET http://localhost:8080/api/progetti/utente/{idUtente}
```

### Crea un nuovo progetto (AUTENTICATO)

> [!note]
> Il progetto viene creato con la B.O.M. (distinta base) vuota: la gestione delle righe della B.O.M. è affidata agli endpoint dedicati più sotto.

```js
POST http://localhost:8080/api/progetti
```

**Permessi:** AUTENTICATO. L'autore è sempre l'utente autenticato che effettua la richiesta.

Permette la creazione di un nuovo progetto, specificando tipologia, nome e descrizione. Il campo `tipo` deve corrispondere a uno dei valori validi (`STAMPA_3D`, `ELETTRONICA`, `ROBOTICA`, `SOFTWARE`).

**Esempio da terminale:**
```bash
curl -X POST http://localhost:8080/api/progetti \
     -H "Authorization: Bearer {{token}}" \
     -H "Content-Type: application/json" \
     -d '{
           "tipo": "STAMPA_3D",
           "nome": "Supporto da tavolo per smartphone",
           "descrizione": "Supporto stampabile in PLA per smartphone e tablet"
         }'
```

### Elimina un progetto a partire dal suo `id` (PROPRIETARIO / ADMIN)

> [!note]
> L'eliminazione di un progetto è distruttiva: cancella automaticamente anche tutte le righe della sua B.O.M. (relazione `BOM` → `RigaBOM` con `cascade = CascadeType.ALL` e `orphanRemoval = true`). Non richiede la rimozione preventiva delle singole righe.

```js
DELETE http://localhost:8080/api/progetti/{idProgetto}
```

**Permessi:** Proprietario o ADMIN.

Consente l'eliminazione del progetto indicato da `{idProgetto}`. Risponde con 204 No Content se la cancellazione ha successo, 403 Forbidden se il progetto non appartiene a chi effettua la richiesta, 404 Not Found se l'id non corrisponde a nessun progetto esistente.

**Esempio da terminale:**
```bash
curl -v -X DELETE http://localhost:8080/api/progetti/{idProgetto} \
     -H "Authorization: Bearer {{token}}"
```

### Aggiungi una riga alla BOM di un progetto (PROPRIETARIO / ADMIN)

> [!note]
> Come per gli articoli in inventario, l'elemento di catalogo richiesto viene indicato tramite il suo `id` (non il nome): va prima recuperato con `GET /api/catalogo`.

```js
POST http://localhost:8080/api/progetti/{idProgetto}/bom
```

**Permessi:** Proprietario del progetto o ADMIN.

Aggiunge una nuova riga alla distinta base del progetto indicato da `{idProgetto}`, specificando l'elemento di catalogo richiesto e la quantità. Risponde con 201 Created e il DTO della riga appena creata, 404 Not Found se il progetto o l'elemento di catalogo non esistono.

**Esempio da terminale:**
```bash
curl -X POST http://localhost:8080/api/progetti/{idProgetto}/bom \
     -H "Authorization: Bearer {{token}}" \
     -H "Content-Type: application/json" \
     -d '{
           "idElementoCatalogo": 1,
           "quantita": 2
         }'
```

### Elimina una riga dalla BOM di un progetto a partire dal suo `id` (PROPRIETARIO / ADMIN)

```js
DELETE http://localhost:8080/api/progetti/{idProgetto}/bom/{idRiga}
```

**Permessi:** Proprietario del progetto o ADMIN.

Consente l'eliminazione della riga indicata da `{idRiga}` dalla B.O.M. del progetto `{idProgetto}`. Risponde con 204 No Content se la cancellazione ha successo, 404 Not Found se il progetto o la riga non esistono (o la riga non appartiene a quel progetto).

**Esempio da terminale:**
```bash
curl -v -X DELETE http://localhost:8080/api/progetti/{idProgetto}/bom/{idRiga} \
     -H "Authorization: Bearer {{token}}"
```
