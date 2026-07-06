package it.unipi.makermanagerserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.unipi.makermanagerserver.enums.RuoloUtente;
import it.unipi.makermanagerserver.model.user.Utente;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {

    // cerca un utente sulla base del suo nickname
    Optional<Utente> findByNickname(String nickname);

    // Cerca un utente per email: e' l'identificativo usato per il login
    Optional<Utente> findByEmail(String email);

    // Verifica in fase di registrazione se email o nickname sono gia' in uso
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

    // utility per l'inizializzazione
    void deleteAllByNicknameNot(String nickname);

    // Usato da AdminBootstrap per verificare se esiste gia' almeno un ADMIN
    boolean existsByRuolo(RuoloUtente ruolo);

}