package it.unipi.makermanagerserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Intercettore globale degli errori per l'applicazione.
 * Converte le eccezioni Java in risposte JSON standardizzate per il Client JavaFX.
 *
 * Ogni handler e' agganciato al TIPO dell'eccezione (non al testo del suo
 * messaggio): questo rende la corrispondenza eccezione -> status HTTP
 * esplicita e stabile, indipendente da come i messaggi vengono scritti nei
 * vari service.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gestisce gli errori generati dalla validazione automatica di @Valid/@Validated
     * nei controller (es. campi @NotBlank, @Min, @NotNull nei DTO di richiesta).
     * Restituisce una mappa con i campi errati e i relativi messaggi di errore.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        // Ritorna HTTP 400 Bad Request con l'elenco dei campi non validi
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Gestisce i casi in cui una risorsa richiesta (Inventario, ElementoCatalogo,
     * ArticoloInventario, ProgettoMaker, ...) non esista nel DB.
     * I service lanciano RisorsaNonTrovataException quando un id passato
     * non corrisponde a nessuna entita' esistente.
     */
    @ExceptionHandler(RisorsaNonTrovataException.class)
    public ResponseEntity<Map<String, String>> handleRisorsaNonTrovata(RisorsaNonTrovataException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("errore", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Gestisce i casi in cui un dato fornito in input non sia valido: valori
     * di enum sconosciuti, riferimenti testuali non risolvibili nel JSON di
     * inizializzazione, e simili. I service lanciano DatiNonValidiException
     * per questi casi.
     */
    @ExceptionHandler(DatiNonValidiException.class)
    public ResponseEntity<Map<String, String>> handleDatiNonValidi(DatiNonValidiException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("errore", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Gestisce le credenziali non valide fornite in fase di login (email
     * inesistente o password errata). Il service lancia sempre
     * CredenzialiNonValideException con lo stesso messaggio generico in
     * entrambi i casi, per non rivelare quale campo sia errato.
     */
    @ExceptionHandler(CredenzialiNonValideException.class)
    public ResponseEntity<Map<String, String>> handleCredenzialiNonValide(CredenzialiNonValideException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("errore", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    /**
     * Gestisce i casi in cui si tenti di creare una risorsa che confligge
     * con una gia' esistente per un vincolo di unicita' (es. email o
     * nickname gia' registrati in fase di registrazione).
     */
    @ExceptionHandler(RisorsaGiaEsistenteException.class)
    public ResponseEntity<Map<String, String>> handleRisorsaGiaEsistente(RisorsaGiaEsistenteException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("errore", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    /**
     * Gestisce i tentativi di un utente autenticato di operare su una
     * risorsa (Inventario, ProgettoMaker, ...) che non gli appartiene,
     * senza avere il ruolo ADMIN per un accesso esteso.
     */
    @ExceptionHandler(AccessoNegatoException.class)
    public ResponseEntity<Map<String, String>> handleAccessoNegato(AccessoNegatoException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("errore", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    /**
     * Fallback per qualsiasi altra eccezione non intercettata dagli handler
     * specifici sopra: bug non previsti, NullPointerException, errori interni
     * generici. Risponde sempre con 500 Internal Server Error: a differenza
     * della versione precedente, NON tenta piu' di indovinare uno status
     * diverso analizzando il testo del messaggio (comportamento fragile e
     * imprevedibile). Il messaggio esposto al client resta generico per non
     * rivelare dettagli implementativi interni.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("errore", "Errore interno del server: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}