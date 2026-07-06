package it.unipi.makermanagerserver.bootstrap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import it.unipi.makermanagerserver.enums.RuoloUtente;
import it.unipi.makermanagerserver.model.user.Utente;
import it.unipi.makermanagerserver.repository.UtenteRepository;

/**
 * All'avvio del server garantisce che esista sempre almeno un utente ADMIN:
 * senza questo bootstrap non ci sarebbe alcun modo di raggiungere gli
 * endpoint riservati ad ADMIN senza modificare a mano il database
 *
 * FONDAMENTALE PER /inizializza
 * 
 * Comportamento:
 *  - se esiste gia' almeno un ADMIN, non fa nulla;
 *  - altrimenti crea un nuovo utente ADMIN con le credenziali di default.
 *
 * Le credenziali di default sono impostate in application.properties
 */
@Component
public class AdminBootstrap implements CommandLineRunner {

    private static final Logger logger = LogManager.getLogger(AdminBootstrap.class);

    private final UtenteRepository utenteRepo;
    private final PasswordEncoder passwordEncoder;
    private final String nicknameDefault;
    private final String emailDefault;
    private final String passwordDefault;

    public AdminBootstrap(
        UtenteRepository utenteRepo,
        PasswordEncoder passwordEncoder,
        @Value("${admin.default.nickname}") String nicknameDefault,
        @Value("${admin.default.email}") String emailDefault,
        @Value("${admin.default.password}") String passwordDefault
    ) {

        this.utenteRepo = utenteRepo;
        this.passwordEncoder = passwordEncoder;
        this.nicknameDefault = nicknameDefault;
        this.emailDefault = emailDefault;
        this.passwordDefault = passwordDefault;

    }

    @Override
    public void run(String... args) {

        if (utenteRepo.existsByRuolo(RuoloUtente.ADMIN)) {
            return;
        }

        Utente admin = new Utente(
            nicknameDefault, 
            emailDefault, 
            passwordEncoder.encode(passwordDefault), 
            RuoloUtente.ADMIN
        );

        utenteRepo.save(admin);
        logger.warn("Nessun utente ADMIN presente: creato admin default.");

    }

}
