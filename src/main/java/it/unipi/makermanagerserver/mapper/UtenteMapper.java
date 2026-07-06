package it.unipi.makermanagerserver.mapper;

import org.springframework.stereotype.Component;

import it.unipi.makermanagerserver.dto.auth.AuthResponseDTO;
import it.unipi.makermanagerserver.dto.auth.RegistrazioneRequestDTO;
import it.unipi.makermanagerserver.dto.utente.UtenteResponseDTO;
import it.unipi.makermanagerserver.enums.RuoloUtente;
import it.unipi.makermanagerserver.model.user.Utente;

/**
 * Mapper dedicato alla conversione tra l'entity Utente e i suoi
 * DTO di richiesta/risposta.
 *
 * @Component la registra come bean Spring, cosi' puo' essere iniettata via
 * costruttore in qualunque service ne abbia bisogno
 */
@Component
public class UtenteMapper {

    /**
     * Crea un nuovo Utente da registrare. La password ricevuta e' gia'
     * stata criptata dal Service (BCryptPasswordEncoder)
     * Ogni nuovo utente registrato riceve automaticamente il ruolo UTENTE:
     * il ruolo ADMIN puo' essere assegnato solo manualmente (es. da DB),
     * inoltre all'avvio viene creato un admin se non esiste (per fare
     * /inizializza)
     */
    public Utente toUtente(RegistrazioneRequestDTO dto, String passwordCriptata) {

        return new Utente(
            dto.getNickname(),
            dto.getEmail(),
            passwordCriptata,
            RuoloUtente.UTENTE
        );

    }

    public UtenteResponseDTO toResponseDTO(Utente utente) {

        return new UtenteResponseDTO(
            utente.getId(),
            utente.getNickname(),
            utente.getEmail(),
            utente.getRuolo().name()
        );

    }

    public AuthResponseDTO toAuthResponseDTO(Utente utente, String token) {

        return new AuthResponseDTO(
            token,
            utente.getId(),
            utente.getNickname(),
            utente.getEmail(),
            utente.getRuolo().name()
        );

    }

}
