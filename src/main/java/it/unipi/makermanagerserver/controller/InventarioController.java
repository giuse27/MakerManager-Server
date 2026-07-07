package it.unipi.makermanagerserver.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.unipi.makermanagerserver.dto.common.AggiornaQuantitaDTO;
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

        return ResponseEntity.ok(inventarioService.trovaTuttiGliInventari());

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

        // Se l'inventario non esiste, il Service lancia NoSuchElementException:
        // la lasciamo risalire, ci pensa il GlobalExceptionHandler a restituire 404
        return ResponseEntity.ok(inventarioService.trovaInventario(idInventario));

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

        return ResponseEntity.ok(inventarioService.trovaInventariUtente(idUtente));
    
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
        
        InventarioResponseDTO dtoRisposta = inventarioService.creaInventario(inventario);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoRisposta);

    }

    /**
     * DELETE /api/inventario/{idInventario}
     * 
     * Elimina l'inventario a partire dal suo id
     */
    @DeleteMapping("/{idInventario}")
    public ResponseEntity<Void> eliminaInventario(@PathVariable Long idInventario) {

        inventarioService.eliminaInventario(idInventario);
        return ResponseEntity.noContent().build();

    }
    
    /**
     * POST /api/inventario/articoli
     * 
     * Crea un nuovo articolo. L'inventario e l'elemento di catalogo di
     * destinazione sono indicati dagli id presenti nel body (idInventario,
     * idElementoCatalogo): a differenza degli altri endpoint qui non c'e'
     * un id "genitore" nel path, perche' un ArticoloInventario ha gia' un
     * proprio id univoco indipendente dall'inventario che lo contiene.
     */
    @PostMapping("/articoli")
    public ResponseEntity<ArticoloInventarioResponseDTO> creaArticoloInventario(
        @Validated @RequestBody ArticoloInventarioRequestDTO articolo
    ) {

        ArticoloInventarioResponseDTO dtoRisposta = inventarioService.creaArticoloInventario(articolo);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoRisposta);

    }

    /**
     * PATCH /api/inventario/articoli/{idArticolo}
     * 
     * Aggiorna la quantità dell'articolo indicato, sostituendo il valore
     * esistente con quello passato nel body.
     */
    @PatchMapping("/articoli/{idArticolo}")
    public ResponseEntity<ArticoloInventarioResponseDTO> aggiornaQuantitaArticolo(
        @PathVariable Long idArticolo,
        @Validated @RequestBody AggiornaQuantitaDTO dto
    ) {

        ArticoloInventarioResponseDTO dtoRisposta = inventarioService.aggiornaQuantitaArticolo(idArticolo, dto);
        return ResponseEntity.ok(dtoRisposta);

    }

    /**
     * DELETE /api/inventario/articoli/{idArticolo}
     * 
     * Elimina l'articolo contrassegnato da idArticolo. Non serve indicare
     * l'inventario di appartenenza: l'id dell'articolo e' gia' una chiave
     * primaria univoca in tutto il database (vedi nota in InventarioService).
     */
    @DeleteMapping("/articoli/{idArticolo}")
    public ResponseEntity<Void> eliminaArticoloDaInventario(
        @PathVariable Long idArticolo
    ) {

        inventarioService.eliminaArticoloDaInventario(idArticolo);
        return ResponseEntity.noContent().build();

    }

}
