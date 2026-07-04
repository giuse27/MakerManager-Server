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