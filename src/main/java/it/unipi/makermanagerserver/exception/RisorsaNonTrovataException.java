package it.unipi.makermanagerserver.exception;

/**
 * Eccezione lanciata quando una risorsa richiesta tramite id
 * (ElementoCatalogo, Inventario, ArticoloInventario, ProgettoMaker, ...)
 * non esiste nel database.
 *
 * A differenza di NoSuchElementException (usata in precedenza), questa
 * e' un'eccezione specifica del dominio dell'applicazione: viene
 * intercettata da GlobalExceptionHandler in base al SUO TIPO, non
 * al testo del suo messaggio, quindi il messaggio puo' essere scritto
 * liberamente senza il rischio di alterare lo status HTTP restituito.
 */
public class RisorsaNonTrovataException extends RuntimeException {

    public RisorsaNonTrovataException(String messaggio) {
        super(messaggio);
    }

}