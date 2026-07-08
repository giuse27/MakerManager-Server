package it.unipi.makermanagerserver.service.business;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import it.unipi.makermanagerserver.dto.raccomandazione.ProgettoConsigliatoResponseDTO;
import it.unipi.makermanagerserver.exception.DatiNonValidiException;
import it.unipi.makermanagerserver.manager.raccomandazioni.CalcolatoreFattibilita;
import it.unipi.makermanagerserver.manager.raccomandazioni.EsitoFattibilita;
import it.unipi.makermanagerserver.mapper.ProgettiConsigliatiMapper;
import it.unipi.makermanagerserver.model.inventory.ArticoloInventario;
import it.unipi.makermanagerserver.model.project.ProgettoMaker;
import it.unipi.makermanagerserver.model.user.Utente;
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

        // per prima cosa recupero l'utente corrente
        Utente utente = utenteCorrente.get();

        // verifico la correttezza della soglia
        int soglia = risolviSoglia(sogliaRichiesta);
        
        // recupero i possedimenti dell'utente in una mappa con idElemento e disp
        // utile per fare i controlli sui progetti successivamente
        Map<Long, Integer> possedimenti = recuperaPossedimenti(utente.getId());

        // recupero i progetti in catalogo applicando dei filtri

        
        return null;

    }

    // METODI PRIVATI PER IL RECUPERO DELLE INFORMAZIONI

    private Map<Long, Integer> recuperaPossedimenti(long idUtente) {

        // cerco tutti di ArticoliInventario posseduti da un utente
        return articoliRepo.findByInventarioUtenteId(idUtente)
                    // apro lo stream per eseguire delle operazioni
                    .stream()
                    // raggruppo per idElemento rimuovendo i duplicati e poi 
                    // sommo le quantita sugli elementi uguali
                    .collect(Collectors.groupingBy(
                        articolo -> articolo.getElementoCatalogo().getId(),
                        Collectors.summingInt(ArticoloInventario::getQuantita)
                    ));
                    // il risultato ottenuto è adesso una lista di id di elementi
                    // univoci di cui per ognuno si conosce la quantita posseduta

    }

    // UTILITY PRIVATE

    /**
     * Verifica se il ProgettoMaker progetto appartiente all'Utente utente
     * 
     * @param progetto progetto da valutare
     * @param utente utente del sistema su cui si sta facendo la valutazione
     * @return true se il progetto appartiene all'utente, false altrimenti
     */
    private boolean appartieneAllUtente(
        ProgettoMaker progetto, Utente utente
    ) {

        return (progetto.getAutore() != null &&
            progetto.getAutore().getId().equals(utente.getId())
        );

    }

    /**
     * Utility per verificare se la bom del progetto è vuota. Faccio questo
     * controllo perché per come ho deciso la logica, i progetti con bom vuota
     * non devono risultare nei risultati di ricerca. Questi progetti potrebbero
     * essere gestiti in futuro come casi particolari, magari progetti didattici
     * che appariranno in filtri diversi.
     * 
     * @param progetto progetto di cui si desidera conoscere lo stato della bom
     * @return true se il progetto ha bom vuota false altrimenti
     */
    private boolean haBomVuota(ProgettoMaker progetto) {

        return progetto.getDistintaBase()
                        .getRigheFabbisogno()
                        .isEmpty();

    }

    /**
     * Utility che verifica se il progetto corrisponde ai criteri di 
     * relizzabilità o rientra comunque nella soglia
     * 
     * @param esito risultato calcolato da CalcolatoreFattibilita
     * @param soglia soglia per la valutazione
     * @return true se il progetto rientra nei criteri, false altrimenti
     */
    private boolean compatibileConSoglia(
        EsitoFattibilita esito, int soglia
    ) {

        return (esito.realizzabile() || esito.righeMancanti() <= soglia);

    }

    /**
     * Applica il calcolo della fattibilità di un progetto a un singolo progetto
     * e restituisce un record che associa il progetto al suo esito
     * 
     * @param progetto progetto su cui effettuare il calcolo
     * @param disponibilita disponibilità totali dell'utente date da
     *                      recuperaPossedimenti()
     * @return coppia di progetto e esito calcolato
     */
    private Valutazione valuta(
        ProgettoMaker progetto, Map<Long, Integer> disponibilita
    ) {

        EsitoFattibilita esito = calcolatore.calcola(
            progetto.getDistintaBase().getRigheFabbisogno(), 
            disponibilita
        );

        return new Valutazione(progetto, esito);

    }

    // UTILITY DI VALIDAZIONE

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

    // STRUTTURE DATI DI APPOGGIO

    /**
     * Record di appoggio per associare al progetto l'esito della sua fattibilità
     * 
     * @param progetto progetto in esame
     * @param esito esito calcolato per progetto
     */
    private record Valutazione(ProgettoMaker progetto, EsitoFattibilita esito) {
    }

    // COMPARATORI

    private Comparator<Valutazione> ordinaFattibilitaDecrescente() {

        return Comparator
                // 1) ordino prima in base se i progetti sono realizzabili o meno
                .comparing(
                    (Valutazione v) -> v.esito().realizzabile())
                    // l'ordine di 1 lo setto con reversed per avere prima true
                    // e poi false (prima i realizzabili poi quelli con soglia)
                    // l'uso di .reversed() non influenza i .thenComparing
                    .reversed()
                // 2) a parità di realizzabilità ordino sulla base di indice
                //      di fattibilita decrescente
                .thenComparing(
                    v -> v.esito().indiceFattibilita(), Comparator.reverseOrder())
                // 3) ...poi sulla base del numero di righe bom mancanti (crescente)
                .thenComparing(
                    v -> v.esito().righeMancanti())
                // 4) ...poi sulla base dei pezzi totali mancanti (crescente)
                .thenComparing(v -> v.esito().pezziMancantiTotali())
                // 5) e solo infine a parità di tutte le condizioni ordino in 
                //      ordine alfabetico
                .thenComparing(
                    v -> v.progetto().getNome(), String.CASE_INSENSITIVE_ORDER);              

    }

}
