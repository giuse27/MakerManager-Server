# Endpoint esposti dal Server

- [Endpoint esposti dal Server](#endpoint-esposti-dal-server)
  - [Inizializzazione](#inizializzazione)
    - [Inizializza il database](#inizializza-il-database)
  - [Catalogo](#catalogo)
    - [Visualizza elementi in catalogo](#visualizza-elementi-in-catalogo)
    - [Aggiungi un elemento al catalogo](#aggiungi-un-elemento-al-catalogo)
    - [Elimina un elemento dal catalogo a partire dal suo `id`](#elimina-un-elemento-dal-catalogo-a-partire-dal-suo-id)
  - [Inventario](#inventario)
    - [Visualizza tutti gli inventari](#visualizza-tutti-gli-inventari)
    - [Visualizza il contenuto di un inventario](#visualizza-il-contenuto-di-un-inventario)
    - [Visualizza gli inventari di un utente](#visualizza-gli-inventari-di-un-utente)
    - [Crea un nuovo inventario](#crea-un-nuovo-inventario)
    - [Elimina un inventario a partire dal suo `id`](#elimina-un-inventario-a-partire-dal-suo-id)
    - [Aggiungi un articolo a un inventario](#aggiungi-un-articolo-a-un-inventario)
    - [Elimina un articolo dall'inventario a partire dal suo `id`](#elimina-un-articolo-dallinventario-a-partire-dal-suo-id)
  - [Progetti](#progetti)
    - [Visualizza tutti i progetti](#visualizza-tutti-i-progetti)
    - [Visualizza un progetto a partire dal suo `id`](#visualizza-un-progetto-a-partire-dal-suo-id)
    - [Visualizza i progetti di una tipologia](#visualizza-i-progetti-di-una-tipologia)
    - [Crea un nuovo progetto](#crea-un-nuovo-progetto)
    - [Elimina un progetto a partire dal suo `id`](#elimina-un-progetto-a-partire-dal-suo-id)
    - [Aggiungi una riga alla BOM di un progetto](#aggiungi-una-riga-alla-bom-di-un-progetto)
    - [Elimina una riga dalla BOM di un progetto a partire dal suo `id`](#elimina-una-riga-dalla-bom-di-un-progetto-a-partire-dal-suo-id)

## Inizializzazione

### Inizializza il database

```js
POST http://localhost:8080/inizializza
```

Permette di inizializzare il database cancellando il suo contenuto e popolandolo con gli elementi, gli inventari, i progetti ecc contenuti nel file di inzializzazione json, posizionato in `src/main/resources/data/catalogo-iniziale.json`

**Esempio da terminale:**
```bash
curl -v -X POST http://localhost:8080/inizializza
```

> N.B. il `-v` non è obbligatorio, serve solo per mostrare l'output completo del server.

## Catalogo

### Visualizza elementi in catalogo

```js
GET http://localhost:8080/api/catalogo
```

Restituisce la lista completa di elementi presenti in catalogo.

**Esempio da terminale:**
```bash
curl -v -X GET http://localhost:8080/api/catalogo
```

### Aggiungi un elemento al catalogo

```js
POST http://localhost:8080/api/catalogo
```

Permette la creazione e l'inserimento di un elemento in catalogo dalle caratteristiche specificate.

**Esempio da terminale:**
```bash
curl -X POST http://localhost:8080/api/catalogo \
     -H "Content-Type: application/json" \
     -d '{
           "nome": "Elemento Test Eliminazione",
           "descrizione": "Creato solo per essere cancellato",
           "tipologia": "COMPONENTE_ELETTRONICO"
         }'
```

### Elimina un elemento dal catalogo a partire dal suo `id`

> [!note]
> L'operazione di eliminazione di un elemento dal catalogo è possibile solo se l'id di quell'elemento non funge da chiave esterna in nessun'altra tabella. Quindi un elemento può essere rimosso dal catalogo se e solo se nessun utente lo possiede nell'inventario e nessun progettto lo contiene nella sua B.O.M. 

```js
DELETE http://localhost:8080/api/catalogo/{id}
```

Consente l'eliminazione di un elemento, a partire dal suo id, dal catalogo a patto che le condizioni descritte prima siano verificate. Bisogna solo sotituire `{id}` con l'id dell'elemento che si vuole rimuovere dal catalogo.

**Esempio da terminale:**
```bash
curl -v -X DELETE http://localhost:8080/api/catalogo/{id}
```

## Inventario

### Visualizza tutti gli inventari

```js
GET http://localhost:8080/api/inventario
```

Restituisce l'elenco di tutti gli inventari presenti nel database.

**Esempio da terminale:**
```bash
curl -v -X GET http://localhost:8080/api/inventario
```

### Visualizza il contenuto di un inventario

```js
GET http://localhost:8080/api/inventario/{idInventario}
```

Restituisce la lista degli articoli contenuti nell'inventario indicato da `{idInventario}`.

**Esempio da terminale:**
```bash
curl -v -X GET http://localhost:8080/api/inventario/{idInventario}
```

### Visualizza gli inventari di un utente

```js
GET http://localhost:8080/api/inventario/utente/{idUtente}
```

Restituisce l'elenco degli inventari associati all'utente indicato da `{idUtente}`.

**Esempio da terminale:**
```bash
curl -v -X GET http://localhost:8080/api/inventario/utente/{idUtente}
```

### Crea un nuovo inventario

```js
POST http://localhost:8080/api/inventario
```

Permette la creazione di un nuovo inventario, specificando il nome e l'id dell'utente proprietario.

**Esempio da terminale:**
```bash
curl -X POST http://localhost:8080/api/inventario \
     -H "Content-Type: application/json" \
     -d '{
           "nome": "Il mio primo inventario",
           "idUtente": 1
         }'
```

### Elimina un inventario a partire dal suo `id`

> [!note]
> L'eliminazione di un inventario è distruttiva: cancella automaticamente anche tutti gli articoli in esso contenuti (relazione `Inventario` → `ArticoloInventario` con `cascade = CascadeType.ALL` e `orphanRemoval = true`). Non richiede la rimozione preventiva dei singoli articoli.

```js
DELETE http://localhost:8080/api/inventario/{idInventario}
```

Consente l'eliminazione dell'inventario indicato da `{idInventario}`, insieme a tutti gli articoli che contiene.

**Esempio da terminale:**
```bash
curl -v -X DELETE http://localhost:8080/api/inventario/{idInventario}
```

### Aggiungi un articolo a un inventario

> [!note]
> A differenza degli altri endpoint di questa sezione, l'inventario di destinazione non viene indicato nell'URL ma nel body della richiesta (campo `idInventario`), perché un `ArticoloInventario` possiede già un proprio `id` univoco indipendente dall'inventario che lo contiene.

```js
POST http://localhost:8080/api/inventario/articoli
```

Permette la creazione di un nuovo articolo in inventario, specificando l'elemento di catalogo di riferimento, l'inventario di destinazione e la quantità.

**Esempio da terminale:**
```bash
curl -X POST http://localhost:8080/api/inventario/articoli \
     -H "Content-Type: application/json" \
     -d '{
           "idElementoCatalogo": 1,
           "idInventario": 1,
           "quantita": 2
         }'
```

### Elimina un articolo dall'inventario a partire dal suo `id`

```js
DELETE http://localhost:8080/api/inventario/articoli/{idArticolo}
```

Consente l'eliminazione dell'articolo indicato da `{idArticolo}`. Non è necessario indicare l'inventario di appartenenza, poiché l'id dell'articolo è già una chiave univoca in tutto il database.

**Esempio da terminale:**
```bash
curl -v -X DELETE http://localhost:8080/api/inventario/articoli/{idArticolo}
```

## Progetti

### Visualizza tutti i progetti

```js
GET http://localhost:8080/api/progetti
```

Restituisce la lista completa dei progetti presenti in catalogo.

**Esempio da terminale:**
```bash
curl -v -X GET http://localhost:8080/api/progetti
```

### Visualizza un progetto a partire dal suo `id`

> [!note]
> A differenza di `GET /api/progetti` (vista sintetica, senza BOM), questo endpoint restituisce tutti i dettagli del progetto inclusa la sua distinta base (`bom`), con l'elenco delle righe che la compongono.

```js
GET http://localhost:8080/api/progetti/{idProgetto}
```

Restituisce il progetto indicato da `{idProgetto}`, comprensivo della sua B.O.M. Risponde con 404 Not Found se l'id non corrisponde a nessun progetto esistente.

**Esempio da terminale:**
```bash
curl -v -X GET http://localhost:8080/api/progetti/{idProgetto}
```

### Visualizza i progetti di una tipologia

```js
GET http://localhost:8080/api/progetti/tipologia/{tipologia}
```

Restituisce l'elenco dei progetti appartenenti alla tipologia indicata (`STAMPA_3D`, `ELETTRONICA`, `ROBOTICA`, `SOFTWARE`).

**Esempio da terminale:**
```bash
curl -v -X GET http://localhost:8080/api/progetti/tipologia/STAMPA_3D
```

### Crea un nuovo progetto

> [!note]
> Il progetto viene creato con la B.O.M. (distinta base) vuota: la gestione delle righe della B.O.M. sarà affidata a endpoint dedicati in uno step successivo.

```js
POST http://localhost:8080/api/progetti
```

Permette la creazione di un nuovo progetto, specificando tipologia, nome e descrizione. Il campo `tipo` deve corrispondere a uno dei valori validi (`STAMPA_3D`, `ELETTRONICA`, `ROBOTICA`, `SOFTWARE`).

**Esempio da terminale:**
```bash
curl -X POST http://localhost:8080/api/progetti \
     -H "Content-Type: application/json" \
     -d '{
           "tipo": "STAMPA_3D",
           "nome": "Supporto da tavolo per smartphone",
           "descrizione": "Supporto stampabile in PLA per smartphone e tablet"
         }'
```

### Elimina un progetto a partire dal suo `id`

> [!note]
> L'eliminazione di un progetto è distruttiva: cancella automaticamente anche tutte le righe della sua B.O.M. (relazione `BOM` → `RigaBOM` con `cascade = CascadeType.ALL` e `orphanRemoval = true`). Non richiede la rimozione preventiva delle singole righe.

```js
DELETE http://localhost:8080/api/progetti/{idProgetto}
```

Consente l'eliminazione del progetto indicato da `{idProgetto}`. Risponde con 204 No Content se la cancellazione ha successo, 404 Not Found se l'id non corrisponde a nessun progetto esistente.

**Esempio da terminale:**
```bash
curl -v -X DELETE http://localhost:8080/api/progetti/{idProgetto}
```

### Aggiungi una riga alla BOM di un progetto

> [!note]
> Come per gli articoli in inventario, l'elemento di catalogo richiesto viene indicato tramite il suo `id` (non il nome): va prima recuperato con `GET /api/catalogo`.

```js
POST http://localhost:8080/api/progetti/{idProgetto}/bom
```

Aggiunge una nuova riga alla distinta base del progetto indicato da `{idProgetto}`, specificando l'elemento di catalogo richiesto e la quantità. Risponde con 201 Created e il DTO della riga appena creata, 404 Not Found se il progetto non esiste.

**Esempio da terminale:**
```bash
curl -X POST http://localhost:8080/api/progetti/{idProgetto}/bom \
     -H "Content-Type: application/json" \
     -d '{
           "idElementoCatalogo": 1,
           "quantita": 2
         }'
```

### Elimina una riga dalla BOM di un progetto a partire dal suo `id`

```js
DELETE http://localhost:8080/api/progetti/{idProgetto}/bom/{idRiga}
```

Consente l'eliminazione della riga indicata da `{idRiga}` dalla B.O.M. del progetto `{idProgetto}`. Risponde con 204 No Content se la cancellazione ha successo, 404 Not Found se il progetto o la riga non esistono (o la riga non appartiene a quel progetto).

**Esempio da terminale:**
```bash
curl -v -X DELETE http://localhost:8080/api/progetti/{idProgetto}/bom/{idRiga}
```