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
 * Generato da Gemini 3.1 Pro
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gestisce gli errori generati dalla validazione automatica di @Valid nei controller.
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
     * Gestisce i casi in cui una risorsa richiesta (Utente, Inventario, Articolo) non esista nel DB.
     * Nota: Puoi lanciare questa eccezione personalizzata (es. ResourceNotFoundException) dal tuo Service.
     */
    @ExceptionHandler(RuntimeException.class) // Puoi raffinarla creando una tua eccezione specifica
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.clear();
        
        // Se nel Service lanci un messaggio specifico, es: "Inventario non trovato"
        if (ex.getMessage() != null && (ex.getMessage().contains("non trovato") || ex.getMessage().contains("NotFound"))) {
            errorResponse.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse); // HTTP 404
        }
        
        // Fallback per altri errori generici a runtime
        errorResponse.put("error", "Errore interno del server: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse); // HTTP 500
    }

}