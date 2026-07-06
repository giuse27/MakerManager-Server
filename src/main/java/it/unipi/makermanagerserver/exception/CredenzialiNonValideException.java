package it.unipi.makermanagerserver.exception;

/**
 * Eccezione lanciata quando le credenziali fornite in fase di login
 * (email, password) non sono valide.
 *
 * Il messaggio resta volutamente generico (identico sia che l'email non
 * corrisponda a nessun utente, sia che la password non corrisponda) per
 * non rivelare quale dei due campi sia errato
 *
 * Viene intercettata da GlobalExceptionHandler in base al SUO TIPO,
 * risultando in una risposta HTTP 401 Unauthorized.
 */
public class CredenzialiNonValideException extends RuntimeException {

    public CredenzialiNonValideException(String messaggio) {
        super(messaggio);
    }

}
