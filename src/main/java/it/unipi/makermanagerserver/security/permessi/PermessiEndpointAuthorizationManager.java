package it.unipi.makermanagerserver.security.permessi;

import java.util.List;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Unico punto di decisione delle autorizzazioni sugli endpoint HTTP,
 * agganciato in SecurityConfig con ".anyRequest().access(...)".
 *
 * Al posto delle classiche regole statiche scritte in Java
 * (.requestMatchers(...).hasRole(...)), qui ogni richiesta viene valutata
 * contro le regole lette da PermessiEndpointRepository (file esterno
 * permessi-endpoint.properties): la prima regola il cui metodo+path
 * corrispondono alla richiesta decide se l'utente e' autorizzato. Se
 * nessuna regola corrisponde, la richiesta viene rifiutata (nega di default:
 * ogni endpoint deve essere esplicitamente elencato nel file).
 */
@Component
public class PermessiEndpointAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private static final Logger logger = LogManager.getLogger(PermessiEndpointAuthorizationManager.class);

    private final PermessiEndpointRepository permessiRepository;

    public PermessiEndpointAuthorizationManager(PermessiEndpointRepository permessiRepository) {
        this.permessiRepository = permessiRepository;
    }

    @Override
    public AuthorizationResult authorize(
        Supplier<? extends Authentication> authentication, 
        RequestAuthorizationContext context
    ) {

        HttpServletRequest request = context.getRequest();
        List<RegolaPermesso> regole = permessiRepository.trovaRegole();

        // scorro tutte le regole
        for (RegolaPermesso regola : regole) {

            // skippo se non corrisponde alla richiesta
            if (!regola.corrisponde(request)) {
                continue;
            }

            // verifica sulla regola corrispondente l'autorizzazione
            boolean consentito = regola.isConsentito(authentication.get());

            if (!consentito) {
                logger.debug(
                    "Accesso negato a {} {} dalla regola [{}]",
                    request.getMethod(), request.getRequestURI(), regola
                );
            }

            return new AuthorizationDecision(consentito);

        }

        logger.warn(
            "Nessuna regola di permesso trovata per {} {}: accesso negato di default",
            request.getMethod(), request.getRequestURI()
        );

        return new AuthorizationDecision(false);

    }

}
