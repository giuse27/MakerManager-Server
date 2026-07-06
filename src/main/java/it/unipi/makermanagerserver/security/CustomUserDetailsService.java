package it.unipi.makermanagerserver.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.unipi.makermanagerserver.model.user.Utente;
import it.unipi.makermanagerserver.repository.UtenteRepository;

/**
 * Carica un Utente dal database per conto di Spring Security, usando
 * l'email come "username". Il ruolo dell'utente viene esposto come
 * autorita' nel formato "ROLE_<RUOLO>" (es. "ROLE_ADMIN"), la convenzione
 * richiesta da hasRole()/requestMatchers(...).hasRole(...) in SecurityConfig.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UtenteRepository utenteRepo;

    public CustomUserDetailsService(UtenteRepository utenteRepo) {
        this.utenteRepo = utenteRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Utente utente = utenteRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                    "Nessun utente trovato con email " + email
                ));

        return org.springframework.security.core.userdetails.User.builder()
                .username(utente.getEmail())
                .password(utente.getPassword())
                .authorities(new SimpleGrantedAuthority("ROLE_" + utente.getRuolo().name()))
                .build();

    }

}
