package it.unipi.makermanagerserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.unipi.makermanagerserver.dto.auth.AuthResponseDTO;
import it.unipi.makermanagerserver.dto.auth.LoginRequestDTO;
import it.unipi.makermanagerserver.dto.auth.RegistrazioneRequestDTO;
import it.unipi.makermanagerserver.service.endpoint.AuthService;

/**
 * Controller REST per la registrazione e il login degli utenti.
 *
 * A differenza degli altri controller questi endpoint sono pubblici
 * (vedi SecurityConfig): non serve alcun token per raggiungerli, dato
 * che il loro scopo e' proprio ottenerne uno.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * POST /auth/registrazione
     *
     * Crea un nuovo Utente con ruolo UTENTE e restituisce subito un
     * token JWT valido, cosi' il client puo' considerarlo autenticato
     * senza un login separato.
     */
    @PostMapping("/registrazione")
    public ResponseEntity<AuthResponseDTO> registrazione(
        @Validated @RequestBody RegistrazioneRequestDTO dto
    ) {

        AuthResponseDTO dtoRisposta = authService.registra(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoRisposta);

    }

    /**
     * POST /auth/login
     *
     * Verifica le credenziali fornite e restituisce un token JWT valido.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
        @Validated @RequestBody LoginRequestDTO dto
    ) {

        AuthResponseDTO dtoRisposta = authService.login(dto);
        return ResponseEntity.ok(dtoRisposta);

    }

}
