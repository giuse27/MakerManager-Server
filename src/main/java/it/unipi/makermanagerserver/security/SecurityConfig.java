package it.unipi.makermanagerserver.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import it.unipi.makermanagerserver.security.permessi.PermessiEndpointAuthorizationManager;

/**
 * Configura la catena di filtri di sicurezza dell'applicazione.
 *
 * L'autenticazione e' stateless (SessionCreationPolicy.STATELESS): non
 * viene mantenuta alcuna sessione HTTP, ogni richiesta viene autenticata
 * da zero a partire dal JWT presente nell'header Authorization (vedi
 * JwtAuthFilter). E' il modello standard per API REST consumate da un
 * client desktop come MakerManager-Client.
 *
 * A differenza delle tipiche configurazioni Spring Security, qui NON ci
 * sono regole ".requestMatchers(...).hasRole(...)" scritte in Java: ogni
 * richiesta viene delegata a PermessiEndpointAuthorizationManager, che
 * decide in base alle regole lette dal file esterno
 * config/permessi-endpoint.properties (vedi quel file per i dettagli e le
 * istruzioni su come modificarlo). E' cosi' che si possono cambiare i
 * permessi di UTENTE e ADMIN su ciascun endpoint senza toccare il codice,
 * e senza nemmeno riavviare il server (il file viene ricaricato in
 * automatico se modificato).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(
        JwtAuthFilter jwtAuthFilter, 
        CustomUserDetailsService userDetailsService
    ) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    // ENCODING DELLA PASSWORD
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // VERIFICA LE CREDENZIALI DI LOGIN
    @Bean
    public AuthenticationProvider authenticationProvider() {

        // user detail service ricerca l'utente nel DB dalla sua email
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;

    }

    // coordina la verifica delle credenziali
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // filtri che le richieste http devono superare per arrivare al server
    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        PermessiEndpointAuthorizationManager permessiEndpointAuthorizationManager
    ) throws Exception {

        http
            // disattivo la protezione csrf
            .csrf(csrf -> csrf.disable())
            // non creo la sessioni (fondamentale per i token di sicurezza)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // delega a PEAM la validazione dei permessi sugli endpoint
            .authorizeHttpRequests(auth -> auth
                .anyRequest().access(permessiEndpointAuthorizationManager)
            )
            // collega il validatore di credenziali
            .authenticationProvider(authenticationProvider())
            // prima di verificare se le credenziali sono valide
            // verifica la validità del token con (vedi JwtAuthFilter)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

}
