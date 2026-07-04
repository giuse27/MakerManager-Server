package it.unipi.makermanagerserver.exception;

/**
 * Eccezione lanciata quando un dato fornito in input (dal body di una
 * richiesta HTTP o dal JSON di inizializzazione) non e' valido: valori
 * di enum sconosciuti (es. tipologia/tipo progetto inesistente),
 * riferimenti testuali che non trovano corrispondenza (es. un nome
 * ElementoCatalogo citato nel JSON di inizializzazione ma mai definito
 * nella sezione "catalogo").
 *
 * Sostituisce l'uso "a mano" di IllegalArgumentException/IllegalStateException:
 * viene intercettata da GlobalExceptionHandler in base al SUO TIPO,
 * risultando in una risposta HTTP 400 Bad Request coerente.
 */
public class DatiNonValidiException extends RuntimeException {

    public DatiNonValidiException(String messaggio) {
        super(messaggio);
    }

}