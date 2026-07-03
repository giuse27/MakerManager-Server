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

import it.unipi.makermanagerserver.dto.inventario.ArticoloInventarioRequestDTO;
import it.unipi.makermanagerserver.dto.inventario.ArticoloInventarioResponseDTO;
import it.unipi.makermanagerserver.dto.inventario.InventarioRequestDTO;
import it.unipi.makermanagerserver.dto.inventario.InventarioResponseDTO;
import it.unipi.makermanagerserver.service.endpoint.InventarioService;

/**
 * Controller REST per la gestione dell'inventario e degli articoli contenuti
 * al suo interno
 * 
 * Il controller delega tutte le operazioni alle classi specifiche
 */
@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    // il service verrà iniettato automaticamente nel costruttore da spring
    // grazie all'annotazione @Service in CatalogoService
    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    /**
     * GET /api/inventario
     * 
     * Restituisce l'elenco di inventari presenti nel database 
     */
    @GetMapping
    public ResponseEntity<List<InventarioResponseDTO>> trovaTuttiGliInventari() {

        // TODO

        return ResponseEntity.ok().build();

    }

    /**
     * GET /api/inventario/{idInventario}
     * 
     * Restituisce il contenuto degli articoli presenti nell'inventario
     * contrassegnato da id
     */
    @GetMapping("/{idInventario}")
    public ResponseEntity<List<ArticoloInventarioResponseDTO>> trovaInventario(
        @PathVariable Long idInventario
    ) {

        // TODO

        return ResponseEntity.ok().build();

    }

    /**
     * GET /api/inventario/utente/{idUtente}
     * 
     * Restituisce l'elenco di inventari associati a un utente
     */
    @GetMapping("/utente/{idUtente}")
    public ResponseEntity<List<InventarioResponseDTO>> trovaInventariUtente(
        @PathVariable Long idUtente
    ) {

        // TODO

        return ResponseEntity.ok().build();
    
    }

    /**
     * POST /api/inventario
     * 
     * Crea un nuovo inventario specificando id dell'utente e nome dell'inventario
     * L'annotazione @Validated intercetta i vincoli del DTO prima di passare al Service.
     */
    @PostMapping
    public ResponseEntity<InventarioResponseDTO> creaInventario(
        @Validated @RequestBody InventarioRequestDTO inventario
    ) {
        
        // TODO

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    /**
     * DELETE /api/inventario/{idInventario}
     * 
     * Elimina l'inventario a partire dal suo id
     */
    @DeleteMapping("/{idInventario}")
    public ResponseEntity<Void> eliminaInventario(@PathVariable Long idInventario) {

        // TODO

        return ResponseEntity.noContent().build();

    }
    
    /**
     * POST /api/inventario/{idInventario}/articoli
     * 
     * Crea un nuovo articolo nell'inventario id, che ha le caratteristiche
     * specificate nel body
     */
    @PostMapping("/{idInventario}/articoli")
    public ResponseEntity<ArticoloInventarioResponseDTO> creaArticoloInventario(
        @PathVariable Long idInventario,
        @Validated @RequestBody ArticoloInventarioRequestDTO articolo
    ) {

        // TODO

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    /**
     * DELETE /api/inventario/{idInventario}/articoli/{idArticolo}
     * 
     * Elimina l'articolo contrassegnato da idArticolo dall'inventario id
     */
    @DeleteMapping("/{idInventario}/articoli/{idArticolo}")
    public ResponseEntity<Void> eliminaArticoloDaInventario(
        @PathVariable Long idInventario,
        @PathVariable Long idArticolo
    ) {

        // TODO

        return ResponseEntity.noContent().build();

    }

}
