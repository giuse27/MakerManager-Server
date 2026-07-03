package it.unipi.makermanagerserver.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.unipi.makermanagerserver.dto.catalogo.ElementoCatalogoRequestDTO;
import it.unipi.makermanagerserver.dto.catalogo.ElementoCatalogoResponseDTO;
import it.unipi.makermanagerserver.service.endpoint.CatalogoService;
 
/**
 * Controller REST per la gestione del Catalogo
 *
 * La classe delega tutte le opperazioni di mapper, dto e service ad altre
 * classi, e quindi funge solo da entry point
 */

@RestController
@RequestMapping("/api/catalogo")
public class CatalogoController {

    private final CatalogoService catalogoService;
    
    // il service verrà iniettato automaticamente nel costruttore da spring
    // grazie all'annotazione @Service in CatalogoService
    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    /**
     * GET /api/catalogo
     * 
     * restituisce la lista completa degli elementi in catalogo
     */
    @GetMapping
    public ResponseEntity<List<ElementoCatalogoResponseDTO>> trovaTutti() {
        return ResponseEntity.ok(catalogoService.trovaTutti());
    }

    /**
     * POST /api/catalogo
     * 
     * crea un nuovo elemento in catalogo. Risponde con 201 Created e il DTO 
     * dell'elemento appena creato (comprensivo dell'id generato dal database).
     */
    @PostMapping
    public ResponseEntity<ElementoCatalogoResponseDTO> crea(
        @RequestBody ElementoCatalogoRequestDTO dtoRichiesta
    ) {

        ElementoCatalogoResponseDTO dtoRisposta = catalogoService.crea(dtoRichiesta);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoRisposta);

    }

    /**
     * DELETE /api/catalogo/{id}
     * 
     * elimina l'elemento in catalogo con l'id indicato.
     * Risponde con 204 No Content se la cancellazione ha successo,
     * 404 Not Found se l'id non corrisponde a nessun elemento esistente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> elimina(@PathVariable Long id) {

        try {

            catalogoService.elimina(id);
            return ResponseEntity.noContent().build();

        } catch (NoSuchElementException e) {

            return ResponseEntity.notFound().build();

        }

    }
    
}
