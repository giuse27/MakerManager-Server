package it.unipi.makermanagerserver.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Intercetta ogni richiesta HTTP e, se presente un header
 * "Authorization: Bearer <token>" con un JWT valido, autentica l'utente
 * per la durata della richiesta popolando il SecurityContext. Se l'header
 * manca o il token non e' valido la richiesta prosegue comunque non
 * autenticata: sara' poi SecurityConfig a decidere se l'endpoint richiesto
 * richiede autenticazione o e' pubblico.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String PREFISSO_BEARER = "Bearer ";

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. verifico la presenza del token
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith(PREFISSO_BEARER)) {
            // anche se manca proseguo lo stesso delegando ad altri
            filterChain.doFilter(request, response);
            return;
        }

        // 2. il token è presente, lo estraggo
        String token = header.substring(PREFISSO_BEARER.length());

        try {

            // delego al service l'estrazione della mail dell'utente
            String email = jwtService.estraiEmail(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Se è riuscito a estrarre l'email e se Spring Security non ha 
                // ancora autenticato l'utente per questa specifica richiesta
                // procedo
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // controllo se il token non è scaduto
                if (jwtService.isTokenValido(token, userDetails.getUsername())) {

                    /*
                        Una volta fatte tutte le verifiche e il token è valido
                        creo un oggetto autenticazione con all'interno
                        l'utente, il suo ruolo e i suoi permessi e metto
                        tutto nel SecurityContextHolder (vedi UtenteCorrente)
                     */
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                }

            }

        } catch (JwtException | IllegalArgumentException ex) {
            // Token malformato, scaduto o con firma non valida: la richiesta
            // prosegue non autenticata, verra' eventualmente rifiutata da
            // SecurityConfig se l'endpoint richiesto richiede autenticazione.
        }

        filterChain.doFilter(request, response);

    }

}
