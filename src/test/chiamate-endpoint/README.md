# Test delle chiamate agli e endpoint

In questa cartella sono presenti dei file di testo che contengono esempi di chiamate agli endpoint esposti dal server. Questi test non sono da intendersi come test che rispettano le specifiche del progetto, ma sono più test temporanei e ripetibili facilmente.

Per poter effettuare tali test è necessario:

* **Visual Studio Code**
* **Estensione: REST Client (Huachao Mao)**

> [!warning]
> **NOTA BENE:** Tutti i test basati su id devono essere ricontrollati ogni volta in quanto gli id sono suscettibili a variazioni.

## Ordine consigliato di esecuzione

Il server richiede autenticazione JWT sulla maggior parte degli endpoint (vedi `docs/varie/endpoint.md`). Per usare questi file:

1. Esegui `http/auth.http`: registra un paio di utenti di test e fai login (anche come admin di default, credenziali in `application.properties`).
2. Copia i `token` restituiti nelle variabili `@token` / `@token2` / `@tokenAdmin` in cima agli altri file `.http` (`utenti.http`, `catalogo.http`, `inventario.http`, `progetti.http`, `inizializzazione.http`).
3. Esegui `http/inizializzazione.http` per popolare il database con i dati di test, poi gli altri file nell'ordine che preferisci.

I token scadono dopo `jwt.expiration-ms` (di default 24h): se una richiesta autenticata inizia a rispondere 401/403 in modo inatteso, rifai il login e aggiorna le variabili.