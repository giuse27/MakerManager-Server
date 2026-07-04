package it.unipi.makermanagerserver.controller;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import it.unipi.makermanagerserver.service.endpoint.InizializzazioneService;

@RestController
public class InizializzazioneController {

    private static final Logger logger = LogManager.getLogger(InizializzazioneController.class);

    private final InizializzazioneService inizializzazioneService;

    public InizializzazioneController(InizializzazioneService inizializzazioneService) {
        this.inizializzazioneService = inizializzazioneService;
    }

    /**
     * POST perche' l'operazione modifica lo stato del server (cancella e
     * ricrea dati): non e' un'operazione idempotente di sola lettura come
     * sarebbe una GET.
     *
     * In caso di richiami successivi su un sistema gia' inizializzato,
     * il comportamento e' quello richiesto dal documento di contesto:
     * cancellazione integrale e ricaricamento dalla sorgente JSON.
     */
    @PostMapping("/inizializza")
    public ResponseEntity<Map<String, String>> inizializza() {

        try {
            
            inizializzazioneService.inizializza();
            return ResponseEntity
                    .ok(Map.of("messaggio", "Database inizializzato con successo."));

        } catch (Exception e) {
            
            logger.error("Errore durante l'inizializzazione del database ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("errore", "Inizializzazione fallita: " + e.getMessage()));

        }

    }
    
}
