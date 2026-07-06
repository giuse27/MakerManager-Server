package it.unipi.makermanagerserver.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.unipi.makermanagerserver.dto.utente.UtenteResponseDTO;
import it.unipi.makermanagerserver.service.endpoint.UtenteService;

/**
 * Controller REST per la consultazione degli Utenti registrati.
 *
 * GET /api/utenti/me e' raggiungibile da qualunque utente autenticato e
 * restituisce il proprio profilo; GET /api/utenti (elenco completo) e'
 * riservato al ruolo ADMIN (vedi SecurityConfig), a dimostrazione del
 * sistema multi ruolo.
 */
@RestController
@RequestMapping("/api/utenti")
public class UtenteController {

    private final UtenteService utenteService;

    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    /**
     * GET /api/utenti
     *
     * Restituisce l'elenco di tutti gli utenti registrati.
     * Riservato al ruolo ADMIN (vedi SecurityConfig).
     */
    @GetMapping
    public ResponseEntity<List<UtenteResponseDTO>> trovaTuttiGliUtenti() {

        return ResponseEntity.ok(utenteService.trovaTutti());

    }

    /**
     * GET /api/utenti/me
     *
     * Restituisce il profilo dell'utente autenticato che ha effettuato
     * la richiesta. L'identita' dell'utente viene ricavata dal JWT
     * validato da JwtAuthFilter, non da un parametro fornito dal client.
     */
    @GetMapping("/me")
    public ResponseEntity<UtenteResponseDTO> profiloCorrente(
        @AuthenticationPrincipal UserDetails userDetails
    ) {

        return ResponseEntity.ok(utenteService.trovaProfilo(userDetails.getUsername()));

    }

}
