package it.unipi.makermanagerserver.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.unipi.makermanagerserver.dto.progetto.ProgettoRequestDTO;
import it.unipi.makermanagerserver.dto.progetto.ProgettoResponseDTO;
import it.unipi.makermanagerserver.service.endpoint.ProgettoService;

/**
 * Controller REST per la gestione dei progetti in catalogo.
 * La classe delega tutte le operazioni a service/mapper e funge solo da entry point.
 */
@RestController
@RequestMapping("/api/progetti")
public class ProgettoController {

    private final ProgettoService progettoService;

    public ProgettoController(ProgettoService progettoService) {
        this.progettoService = progettoService;
    }

    /**
     * GET /api/progetti
     * 
     * trova tutti i progetti in catalogo
     */
    @GetMapping
    public ResponseEntity<List<ProgettoResponseDTO>> trovaTuttiIProgetti() {

        return ResponseEntity.ok(progettoService.trovaTutti());

    }
    
    /**
     * GET /api/progetti/{idProgetto}
     * 
     * trova un progetto a partire dal suo id.
     * Se l'id non corrisponde a nessun progetto esistente, il service lancia
     * RisorsaNonTrovataException: la lasciamo risalire, ci pensa il
     * GlobalExceptionHandler a restituire 404 con corpo JSON coerente
     * con tutti gli altri endpoint del progetto.
     */
    @GetMapping("/{idProgetto}")
    public ResponseEntity<ProgettoResponseDTO> trovaProgetto(
        @PathVariable Long idProgetto
    ) {

        return ResponseEntity.ok(progettoService.trovaPerId(idProgetto));

    }

    /**
     * GET /api/progetti/tipologia/{tipologia}
     * 
     * resituisce tutti i progetti di una certa tipologia
     */
    @GetMapping("/tipologia/{tipologia}")
    public ResponseEntity<List<ProgettoResponseDTO>> trovaProgettiPerTipologia(
        @PathVariable String tipologia
    ) {

        return ResponseEntity.ok(progettoService.trovaPerTipologia(tipologia));

    }

    /**
     * POST /api/progetti
     * 
     * crea un nuovo progetto dalle caratteristiche specificate nel body
     * il progetto ha inizialmente BOM vuota
     * Risponde con 201 e il dto creato
     */
    @PostMapping
    public ResponseEntity<ProgettoResponseDTO> creaProgetto(
        @Validated @RequestBody ProgettoRequestDTO dtoRichiesta
    ) {

        ProgettoResponseDTO dtoRisposta = progettoService.crea(dtoRichiesta);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoRisposta);

    }

    /**
     * DELETE /api/progetti/{id}
     * 
     * elimina il progetto con l'id indicato.
     * Risponde 204 No Content se la cancellazione ha successo.
     * Se l'id non corrisponde a nessun progetto esistente, il service lancia
     * RisorsaNonTrovataException: la lasciamo risalire al
     * GlobalExceptionHandler, che risponde con 404 e corpo JSON coerente.
     */
    @DeleteMapping("/{idProgetto}")
    public ResponseEntity<Void> eliminaProgetto(@PathVariable Long idProgetto) {

        progettoService.elimina(idProgetto);
        return ResponseEntity.noContent().build();

    }

    
}
