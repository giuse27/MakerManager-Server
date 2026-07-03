package it.unipi.makermanagerserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * GET /api/inventario/{idUtente}
     * 
     * Restituisce l'elenco di inventari associati a un utente
     */

    /**
     * POST /api/inventario
     * 
     * Crea un nuovo inventario specificando id dell'utente e nome dell'inventario
     */

    /**
     * DELETE /api/inventario/{id}
     * 
     * Elimina l'inventario a partire dal suo id
     */

    /**
     * GET /api/inventario/{id}/articoli
     * 
     * Restituisce il contenuto degli articoli presenti nell'inventario
     * contrassegnato da id
     */
    
    /**
     * POST /api/inventario/{id}/articoli
     * 
     * Crea un nuovo articolo nell'inventario id, che ha le caratteristiche
     * specificate nel body
     */

    /**
     * DELETE /api/inventario/{id}/articoli/{idArticolo}
     * 
     * Elimina l'articolo contrassegnato da idArticolo dall'inventario id
     */
    
}
