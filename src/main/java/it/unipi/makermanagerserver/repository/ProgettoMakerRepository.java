package it.unipi.makermanagerserver.repository;

import it.unipi.makermanagerserver.enums.TipologiaProgetto;
import it.unipi.makermanagerserver.model.project.ProgettoMaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProgettoMakerRepository extends JpaRepository<ProgettoMaker, Long> {
    
    // Cerca progetti per nome
    List<ProgettoMaker> findByNomeContainingIgnoreCase(String nome);
    
    // JPA permette di navigare dentro gli oggetti @Embedded
    // Questo metodo cerca i progetti in base allo stato contenuto dentro la classe Progresso
    List<ProgettoMaker> findByProgressoStatoAvanzamento(String stato);

    // trova progetti per tipologia
    List<ProgettoMaker> findByTipologia(TipologiaProgetto tipologia);

    // trova i progetti creati da un utente
    List<ProgettoMaker> findByAutoreId(Long idUtente);

}