# Endpoint esposti dal Server

- [Endpoint esposti dal Server](#endpoint-esposti-dal-server)
  - [Inizializzazione](#inizializzazione)
    - [Inizializza il database](#inizializza-il-database)
  - [Catalogo](#catalogo)
    - [Visualizza elementi in catalogo](#visualizza-elementi-in-catalogo)
    - [Aggiungi un elemento al catalogo](#aggiungi-un-elemento-al-catalogo)
    - [Elimina un elemento dal catalogo a partire dal suo `id`](#elimina-un-elemento-dal-catalogo-a-partire-dal-suo-id)

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


