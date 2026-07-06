package it.unipi.makermanagerserver.exception;

/**
 * Eccezione lanciata quando un utente autenticato tenta un'operazione su
 * una risorsa (Inventario, ProgettoMaker, ...) che non gli appartiene e
 * per cui non ha il ruolo ADMIN.
 *
 * A differenza del filtro di sicurezza a livello di endpoint, questo 
 * controllo avviene nel Service perche' dipende dal dato specifico richiesto,
 * non solo dal path dell'URL.
 *
 * Viene intercettata da GlobalExceptionHandler in base al SUO TIPO,
 * risultando in una risposta HTTP 403 Forbidden.
 */
public class AccessoNegatoException extends RuntimeException {

    public AccessoNegatoException(String messaggio) {
        super(messaggio);
    }

}
