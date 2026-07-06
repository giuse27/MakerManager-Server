package it.unipi.makermanagerserver.service.endpoint;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.unipi.makermanagerserver.dto.auth.AuthResponseDTO;
import it.unipi.makermanagerserver.dto.auth.LoginRequestDTO;
import it.unipi.makermanagerserver.dto.auth.RegistrazioneRequestDTO;
import it.unipi.makermanagerserver.exception.CredenzialiNonValideException;
import it.unipi.makermanagerserver.exception.RisorsaGiaEsistenteException;
import it.unipi.makermanagerserver.mapper.UtenteMapper;
import it.unipi.makermanagerserver.model.user.Utente;
import it.unipi.makermanagerserver.repository.UtenteRepository;
import it.unipi.makermanagerserver.security.JwtService;

/**
 * Questa classe fornisce metodi per l'endpoint /auth/*
 *
 * Il controller usa questa classe come intermediaria con il repository,
 * il mapper, la codifica delle password e la generazione dei token JWT.
 */
@Service
public class AuthService {

    private final UtenteRepository utenteRepo;
    private final UtenteMapper utenteMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
        UtenteRepository utenteRepo,
        UtenteMapper utenteMapper,
        PasswordEncoder passwordEncoder,
        JwtService jwtService
    ) {

        this.utenteRepo = utenteRepo;
        this.utenteMapper = utenteMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;

    }

    /**
     * Registra un nuovo Utente con ruolo UTENTE, criptando la password
     * ricevuta prima di salvarla, e restituisce subito un token JWT:
     * l'utente risulta autenticato appena registrato, senza dover
     * effettuare un login separato.
     *
     * @param dto dati del nuovo utente (nickname, email, password)
     * @return token JWT e dati essenziali dell'utente appena creato
     * @throws RisorsaGiaEsistenteException se l'email o il nickname sono gia' in uso
     */
    public AuthResponseDTO registra(RegistrazioneRequestDTO dto) {

        if (utenteRepo.existsByEmail(dto.getEmail())) {
            throw new RisorsaGiaEsistenteException(
                "Un utente con email " + dto.getEmail() + " è già registrato"
            );
        }
        if (utenteRepo.existsByNickname(dto.getNickname())) {
            throw new RisorsaGiaEsistenteException(
                "Il nickname " + dto.getNickname() + " è già in uso"
            );
        }

        String passwordCriptata = passwordEncoder.encode(dto.getPassword());
        Utente utente = utenteMapper.toUtente(dto, passwordCriptata);
        Utente utenteDB = utenteRepo.save(utente);

        String token = jwtService.generaToken(utenteDB);
        return utenteMapper.toAuthResponseDTO(utenteDB, token);

    }

    /**
     * Verifica le credenziali fornite e restituisce un token JWT valido.
     *
     * @param dto credenziali fornite dal client (email, password)
     * @return token JWT e dati essenziali dell'utente autenticato
     * @throws CredenzialiNonValideException se email o password non sono corrette
     */
    public AuthResponseDTO login(LoginRequestDTO dto) {

        Utente utente = utenteRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new CredenzialiNonValideException(
                    "Credenziali non valide")
                );

        if (!passwordEncoder.matches(dto.getPassword(), utente.getPassword())) {
            throw new CredenzialiNonValideException("Credenziali non valide");
        }

        String token = jwtService.generaToken(utente);
        return utenteMapper.toAuthResponseDTO(utente, token);

    }

}
