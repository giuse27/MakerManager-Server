package it.unipi.makermanagerserver.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import it.unipi.makermanagerserver.enums.RuoloUtente;
import it.unipi.makermanagerserver.exception.AccessoNegatoException;
import it.unipi.makermanagerserver.model.user.Utente;
import it.unipi.makermanagerserver.repository.UtenteRepository;

/**
 * Punto unico per recuperare l'Utente autenticato che ha effettuato la
 * richiesta corrente, e per verificare la proprieta' di una risorsa
 * (Inventario, ProgettoMaker, ...) nei Service.
 *
 * L'identita' viene ricavata dal SecurityContext popolato da JwtAuthFilter
 * (subject del JWT = email dell'utente), non da un parametro fornito dal
 * client: e' cosi' che si evita che un utente possa impersonare un altro
 * semplicemente indicando un id diverso nel body/path di una richiesta.
 */
@Component
public class UtenteCorrente {

    private final UtenteRepository utenteRepo;

    public UtenteCorrente(UtenteRepository utenteRepo) {
        this.utenteRepo = utenteRepo;
    }

    /**
     * Restituisce l'Utente autenticato corrente.
     *
     * @throws AccessoNegatoException se non c'e' alcun utente autenticato
     *         (non dovrebbe accadere sugli endpoint protetti da SecurityConfig)
     */
    public Utente get() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessoNegatoException("Nessun utente autenticato");
        }

        String email = auth.getName();
        return utenteRepo.findByEmail(email)
                .orElseThrow(() -> new AccessoNegatoException(
                    "Utente autenticato non trovato: " + email)
            );

    }

    /**
     * Verifica che l'utente corrente sia il proprietario della risorsa
     * indicata oppure un ADMIN. Una risorsa senza proprietario (es. dati di
     * test caricati da /inizializza, dove proprietario e' null) e'
     * gestibile solo da ADMIN.
     *
     * @throws AccessoNegatoException se nessuna delle due condizioni e' soddisfatta
     */
    public void verificaProprietarioOAdmin(Utente proprietario, String messaggioErrore) {

        Utente corrente = get();

        boolean isProprietario = proprietario != null && proprietario.getId().equals(corrente.getId());
        boolean isAdmin = corrente.getRuolo() == RuoloUtente.ADMIN;

        if (!isProprietario && !isAdmin) {
            throw new AccessoNegatoException(messaggioErrore);
        }

    }

    /**
     * Verifica che l'utente corrente sia proprio l'utente indicato da
     * idUtente oppure un ADMIN. 
     *
     * @throws AccessoNegatoException se nessuna delle due condizioni e' soddisfatta
     */
    public void verificaSeStessoOAdmin(Long idUtente, String messaggioErrore) {

        Utente corrente = get();

        boolean isSeStesso = corrente.getId().equals(idUtente);
        boolean isAdmin = corrente.getRuolo() == RuoloUtente.ADMIN;

        if (!isSeStesso && !isAdmin) {
            throw new AccessoNegatoException(messaggioErrore);
        }

    }

}
