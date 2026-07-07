package it.unipi.makermanagerserver.service.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import it.unipi.makermanagerserver.dto.raccomandazione.ProgettoConsigliatoResponseDTO;
import it.unipi.makermanagerserver.exception.DatiNonValidiException;
import it.unipi.makermanagerserver.manager.raccomandazioni.CalcolatoreFattibilita;
import it.unipi.makermanagerserver.mapper.ProgettiConsigliatiMapper;
import it.unipi.makermanagerserver.repository.ArticoloInventarioRepository;
import it.unipi.makermanagerserver.repository.ProgettoMakerRepository;
import it.unipi.makermanagerserver.security.UtenteCorrente;

/**
 * ProgettiConsigliatiService
 */
@Service
public class ProgettiConsigliatiService {

    private final ProgettoMakerRepository progettoRepo;
    private final ArticoloInventarioRepository articoliRepo;
    private final CalcolatoreFattibilita calcolatore;
    private final ProgettiConsigliatiMapper consigliatiMapper;
    private final UtenteCorrente utenteCorrente;
    private final int sogliaMancantiDefault;

    public ProgettiConsigliatiService(
        ProgettoMakerRepository progettoRepo,
        ArticoloInventarioRepository articoliRepo,
        CalcolatoreFattibilita calcolatore,
        ProgettiConsigliatiMapper consigliatiMapper,
        UtenteCorrente utenteCorrente,
        @Value("${soglia.progetti.consigliati}") int sogliaMancantiDefault
    ) {
        this.progettoRepo = progettoRepo;
        this.articoliRepo = articoliRepo;
        this.calcolatore = calcolatore;
        this.consigliatiMapper = consigliatiMapper;
        this.utenteCorrente = utenteCorrente;
        this.sogliaMancantiDefault = sogliaMancantiDefault;
    }

    /**
     * Metodo del service per trovare i progetti consigliati
     * 
     * Vengono esclusi: i progetti di cui l'utente e' autore (consigliamo cose
     * nuove, non le sue) e i progetti con BOM vuota (non avrebbero senso come
     * suggerimento). Restano poi solo i progetti realizzabili subito oppure a
     * cui manca un numero di elementi distinti minore o uguale alla soglia.
     * 
     * @param sogliaRichiesta soglia opzionale passata dal controller
     * @return progetti consigliati già ordinati
     * @throws DatiNonValidiException se la soglia indicata e' negativa
     */
    public List<ProgettoConsigliatoResponseDTO> consigliaProgetti(
        Integer sogliaRichiesta
    ) {

        int soglia = risolviSoglia(sogliaRichiesta);
        
        return null;

    }

    // UTILITY PRIVATE

    /**
     * Sceglie la soglia effettiva quella passata dal client se presente,
     * altrimenti il default configurato. Rifiuta valori negativi.
     */
    private int risolviSoglia(Integer sogliaRichiesta) {
 
        int soglia = (sogliaRichiesta != null) ? sogliaRichiesta : sogliaMancantiDefault;
 
        if (soglia < 0) {
            throw new DatiNonValidiException(
                "La soglia dei pezzi mancanti non puo' essere negativa (ricevuto: " + soglia + ")"
            );
        }
 
        return soglia;
 
    }

}
