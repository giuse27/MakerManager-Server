package it.unipi.makermanagerserver.service.endpoint;

import java.util.List;

import org.springframework.stereotype.Service;

import it.unipi.makermanagerserver.dto.utente.UtenteResponseDTO;
import it.unipi.makermanagerserver.exception.RisorsaNonTrovataException;
import it.unipi.makermanagerserver.mapper.UtenteMapper;
import it.unipi.makermanagerserver.model.user.Utente;
import it.unipi.makermanagerserver.repository.UtenteRepository;

/**
 * Questa classe fornisce metodi per l'endpoint /api/utenti/*
 *
 * Il controller usa questa classe come intermediaria con il repository o mapper
 */
@Service
public class UtenteService {

    private final UtenteRepository utenteRepo;
    private final UtenteMapper utenteMapper;

    public UtenteService(UtenteRepository utenteRepo, UtenteMapper utenteMapper) {
        this.utenteRepo = utenteRepo;
        this.utenteMapper = utenteMapper;
    }

    /**
     * Restituisce tutti gli utenti registrati. Riservato al ruolo ADMIN
     * (vedi SecurityConfig).
     */
    public List<UtenteResponseDTO> trovaTutti() {

        return utenteRepo.findAll()
                    .stream()
                    .map(utenteMapper::toResponseDTO)
                    .toList();

    }
    
    /**
     * Restituisce il profilo dell'utente autenticato che ha effettuato la richiesta.
     *
     * @param email email dell'utente autenticato (estratta dal JWT da JwtAuthFilter)
     * @throws RisorsaNonTrovataException se l'utente non esiste piu' nel database
     */
    public UtenteResponseDTO trovaProfilo(String email) {

        Utente utente = utenteRepo.findByEmail(email)
                .orElseThrow(() -> new RisorsaNonTrovataException(
                    "Utente con email " + email + " non trovato"
                ));

        return utenteMapper.toResponseDTO(utente);

    }

}
