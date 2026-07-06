package it.unipi.makermanagerserver.exception;

/**
 * Eccezione lanciata quando si tenta di creare una risorsa che va in conflitto
 * con una gia' esistente per un vincolo di unicita' (es. email o nickname
 * gia' registrati in fase di registrazione).
 *
 * Viene intercettata da GlobalExceptionHandler in base al SUO TIPO,
 * risultando in una risposta HTTP 409 Conflict.
 */
public class RisorsaGiaEsistenteException extends RuntimeException {

    public RisorsaGiaEsistenteException(String messaggio) {
        super(messaggio);
    }

}
