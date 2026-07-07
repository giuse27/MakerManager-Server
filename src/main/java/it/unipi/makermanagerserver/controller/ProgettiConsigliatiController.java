package it.unipi.makermanagerserver.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.unipi.makermanagerserver.dto.raccomandazione.ProgettoConsigliatoResponseDTO;
import it.unipi.makermanagerserver.service.business.ProgettiConsigliatiService;

/**
 * Controller rest per la richiesta di progetti consigliati all'utente sulla
 * base del contenuto dei suoi inventari. Questo controller lo ho separato da
 * ProgettoController per non appensantire troppo il codice e per separare
 * le differenti funzioni
 */
@RestController
@RequestMapping("/api/progetti/consigliati")
public class ProgettiConsigliatiController {

    private final ProgettiConsigliatiService consigliatiService;

    public ProgettiConsigliatiController(
        ProgettiConsigliatiService consigliatiService
    ) {
        this.consigliatiService = consigliatiService;
    }

    /**
     * GET /api/progetti/consigliati[?sogliaMancanti=N]
     * 
     * Restituisce i progetti consigliati per l'utente autenticato, ordinati
     * per indice di fattibilita' decrescente.
     * 
     * @param sogliaMancanti parametro opzionale che definisce la soglia per cui
     *                  un progetto può essere consigliato. La soglia conteggia
     *                  il numero di elementi distinti mancanti ed è configurata
     *                  di default in application.properties
     * @return Elenco di progetti consigliati per l'utente corrente
     */
    @GetMapping
    public ResponseEntity<List<ProgettoConsigliatoResponseDTO>> trovaProgettiConsigliati(
        @RequestParam(name = "sogliaMancanti", required = false) Integer sogliaMancanti
    ) {

        // TODO
        return null;

    }
    
}
