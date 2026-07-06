package it.unipi.makermanagerserver.security.permessi;

import java.util.List;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.AntPathMatcher;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Una singola regola letta dal file di configurazione permesso-endpoint.properties:
 * descrive quali ruoli possono raggiungere un dato verbo HTTP + pattern di path.
 *
 * Valori speciali ammessi nell'elenco ruoli:
 *   PUBBLICO    -> chiunque, anche senza autenticazione
 *   AUTENTICATO -> qualsiasi utente che abbia effettuato il login (UTENTE o ADMIN)
 *   ADMIN/UTENTE (o altri valori di Ruolo) -> solo utenti con quel ruolo specifico
 */
public class RegolaPermesso {

    public static final String PUBBLICO = "PUBBLICO";
    public static final String AUTENTICATO = "AUTENTICATO";
    private static final String QUALSIASI_METODO = "ANY";

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private final String metodo;
    private final String pattern;
    private final List<String> ruoli;

    public RegolaPermesso(String metodo, String pattern, List<String> ruoli) {
        this.metodo = metodo.trim().toUpperCase();
        this.pattern = pattern.trim();
        this.ruoli = ruoli;
    }

    /**
     * Vero se questa regola si applica alla richiesta indicata (stesso
     * verbo HTTP, o ANY, e path che rispetta il pattern Ant-style).
     */
    public boolean corrisponde(HttpServletRequest request) {

        boolean metodoCorrisponde = QUALSIASI_METODO.equals(metodo)
                || metodo.equalsIgnoreCase(request.getMethod());

        return metodoCorrisponde && PATH_MATCHER.match(pattern, request.getRequestURI());

    }

    /**
     * Vero se l'utente (autenticato o anonimo) rispetta almeno uno dei
     * ruoli ammessi da questa regola.
     */
    public boolean isConsentito(Authentication authentication) {

        boolean autenticato = authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);

        for (String ruoloGrezzo : ruoli) {

            String ruolo = ruoloGrezzo.trim().toUpperCase();

            if (PUBBLICO.equals(ruolo)) {
                return true;
            }
            if (AUTENTICATO.equals(ruolo)) {
                if (autenticato) {
                    return true;
                }
                continue;
            }
            if (autenticato && haRuolo(authentication, ruolo)) {
                return true;
            }

        }

        return false;

    }

    private boolean haRuolo(Authentication authentication, String ruolo) {

        String autorita = "ROLE_" + ruolo;
        for (GrantedAuthority concessa : authentication.getAuthorities()) {
            if (concessa.getAuthority().equals(autorita)) {
                return true;
            }
        }
        return false;

    }

    @Override
    public String toString() {
        return metodo + " " + pattern + " -> " + ruoli;
    }

}
