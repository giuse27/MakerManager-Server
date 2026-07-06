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

import it.unipi.makermanagerserver.dto.progetto.ProgettoConBomResponseDTO;
import it.unipi.makermanagerserver.dto.progetto.ProgettoRequestDTO;
import it.unipi.makermanagerserver.dto.progetto.ProgettoResponseDTO;
import it.unipi.makermanagerserver.dto.progetto.RigaBOMRequestDTO;
import it.unipi.makermanagerserver.dto.progetto.RigaBOMResponseDTO;
import it.unipi.makermanagerserver.service.endpoint.ProgettoService;

/**
 * Controller REST per la gestione dei progetti in catalogo e della loro B.O.M.
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
     * trova tutti i progetti in catalogo. Vista sintetica: non include la BOM,
     * per non appesantire la risposta quando servono solo le caratteristiche
     * principali (vedi ProgettoConBomResponseDTO per il dettaglio completo).
     */
    @GetMapping
    public ResponseEntity<List<ProgettoResponseDTO>> trovaTuttiIProgetti() {

        return ResponseEntity.ok(progettoService.trovaTutti());

    }
    
    /**
     * GET /api/progetti/{idProgetto}
     * 
     * trova un progetto a partire dal suo id, restituendone tutti i dettagli
     * inclusa la B.O.M.
     */
    @GetMapping("/{idProgetto}")
    public ResponseEntity<ProgettoConBomResponseDTO> trovaProgetto(
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
     * GET /api/progetti/utente/{idUtente}
     *
     * restituisce i progetti appartenenti a un utente. Lettura pubblica:
     * i progetti compongono un catalogo condiviso, non un dato personale
     * (a differenza degli inventari).
     */
    @GetMapping("/utente/{idUtente}")
    public ResponseEntity<List<ProgettoResponseDTO>> trovaProgettiUtente(
        @PathVariable Long idUtente
    ) {

        return ResponseEntity.ok(progettoService.trovaPerUtente(idUtente));

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
     * Risponde 204 No Content se la cancellazione ha successo,
     * 404 Not Found se l'id non corrisponde a nessun progetto esistente.
     */
    @DeleteMapping("/{idProgetto}")
    public ResponseEntity<Void> eliminaProgetto(@PathVariable Long idProgetto) {

        progettoService.elimina(idProgetto);
        return ResponseEntity.noContent().build();

    }

    /**
     * POST /api/progetti/{idProgetto}/bom
     * 
     * aggiunge una nuova riga alla B.O.M. del progetto indicato.
     * Risponde con 201 e il dto della riga creata, 404 se il progetto non esiste.
     */
    @PostMapping("/{idProgetto}/bom")
    public ResponseEntity<RigaBOMResponseDTO> aggiungiRigaBOM(
        @PathVariable Long idProgetto,
        @Validated @RequestBody RigaBOMRequestDTO dtoRichiesta
    ) {

        RigaBOMResponseDTO dtoRisposta = progettoService.aggiungiRigaBOM(idProgetto, dtoRichiesta);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoRisposta);

    }

    /**
     * DELETE /api/progetti/{idProgetto}/bom/{idRiga}
     * 
     * elimina la riga indicata dalla B.O.M. del progetto.
     * Risponde 204 No Content se la cancellazione ha successo,
     * 404 Not Found se il progetto o la riga non esistono.
     */
    @DeleteMapping("/{idProgetto}/bom/{idRiga}")
    public ResponseEntity<Void> eliminaRigaBOM(
        @PathVariable Long idProgetto,
        @PathVariable Long idRiga
    ) {

        progettoService.eliminaRigaBOM(idProgetto, idRiga);
        return ResponseEntity.noContent().build();

    }

}