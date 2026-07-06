package it.unipi.makermanagerserver.service.endpoint;

import java.util.List;

import org.springframework.stereotype.Service;

import it.unipi.makermanagerserver.dto.progetto.ProgettoConBomResponseDTO;
import it.unipi.makermanagerserver.dto.progetto.ProgettoRequestDTO;
import it.unipi.makermanagerserver.dto.progetto.ProgettoResponseDTO;
import it.unipi.makermanagerserver.dto.progetto.RigaBOMRequestDTO;
import it.unipi.makermanagerserver.dto.progetto.RigaBOMResponseDTO;
import it.unipi.makermanagerserver.enums.TipologiaProgetto;
import it.unipi.makermanagerserver.exception.DatiNonValidiException;
import it.unipi.makermanagerserver.exception.RisorsaNonTrovataException;
import it.unipi.makermanagerserver.mapper.ProgettoMapper;
import it.unipi.makermanagerserver.model.project.BOM;
import it.unipi.makermanagerserver.model.project.ProgettoMaker;
import it.unipi.makermanagerserver.model.project.RigaBOM;
import it.unipi.makermanagerserver.model.user.Utente;
import it.unipi.makermanagerserver.repository.ProgettoMakerRepository;
import it.unipi.makermanagerserver.security.UtenteCorrente;

/**
 * Fornisce i metodi per l'endpoint /api/progetti.
 * Il controller usa questa classe come intermediaria con repository e mapper.
 *
 * I progetti compongono un catalogo condiviso: la lettura (trovaTutti,
 * trovaPerId, trovaPerTipologia, trovaPerUtente) resta pubblica per
 * chiunque. Solo le operazioni che modificano un progetto (creazione,
 * eliminazione, modifica della BOM) sono legate alla proprieta': un
 * progetto appartiene sempre a chi lo ha creato, e solo lui (o un ADMIN)
 * puo' modificarlo o eliminarlo (vedi UtenteCorrente).
 */
@Service
public class ProgettoService {

    private final ProgettoMakerRepository progettoRepo;
    private final ProgettoMapper progettoMapper;
    private final UtenteCorrente utenteCorrente;

    public ProgettoService(
        ProgettoMakerRepository progettoRepo,
        ProgettoMapper progettoMapper,
        UtenteCorrente utenteCorrente
    ) {

        this.progettoRepo = progettoRepo;
        this.progettoMapper = progettoMapper;
        this.utenteCorrente = utenteCorrente;

    }

    /**
     * @return Restituisce tutti i progetti dto in catalogo (vista sintetica,
     *         senza BOM)
     */
    public List<ProgettoResponseDTO> trovaTutti() {
        
        return progettoRepo.findAll()
                .stream()
                .map(progettoMapper::toResponseDTO)
                .toList();

    }

    /**
     * Restituisce il progetto dto contrassegnato da idProgetto, comprensivo
     * di tutti i dettagli inclusa la B.O.M.
     * 
     * @param idProgetto id del progetto da visualizzare
     * @throws RisorsaNonTrovataException se l'id non corrisponde a nessun progetto esistente
     */
    public ProgettoConBomResponseDTO trovaPerId(Long idProgetto) {

        ProgettoMaker progetto = cercaProgettoDaId(idProgetto);
        return progettoMapper.toResponseDTOConBom(progetto);

    }

    /**
     * 
     * @param tipologia tipologia di progetti da cercare
     * @return restituisce tutti i progetti appartenenti a una certa tipologia
     * @throws DatiNonValidiException se la tipologia indicata non e' valida
     */
    public List<ProgettoResponseDTO> trovaPerTipologia(String tipologia) {

        TipologiaProgetto tipologiaValida;
        try {
            tipologiaValida = TipologiaProgetto.valueOf(tipologia);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new DatiNonValidiException(
                "Tipologia '" + tipologia + "' non valida. Valori ammessi: "
                + java.util.Arrays.toString(TipologiaProgetto.values())
            );
        }

        return progettoRepo.findByTipologia(tipologiaValida)
                .stream()
                .map(progettoMapper::toResponseDTO)
                .toList();

    }

    /**
     * Restituisce i progetti appartenenti a un utente. A differenza degli
     * inventari, e' una lettura pubblica
     *
     * @param idUtente id dell'utente di cui cercare i progetti
     */
    public List<ProgettoResponseDTO> trovaPerUtente(Long idUtente) {

        return progettoRepo.findByAutoreId(idUtente)
                .stream()
                .map(progettoMapper::toResponseDTO)
                .toList();

    }

    /**
     * Crea un nuovo progetto. Il proprietario e' sempre l'utente
     * autenticato che ha effettuato la richiesta (vedi UtenteCorrente)
     *
     * @param dtoRichiesta body del post con le caratteristiche del progetto
     * @return Restituisce il dto di risposta del progetto creato
     */
    public ProgettoResponseDTO crea(ProgettoRequestDTO dtoRichiesta) {

        Utente autore = utenteCorrente.get();

        ProgettoMaker progetto = progettoMapper.toProgetto(dtoRichiesta, autore);

        ProgettoMaker progettoDB = progettoRepo.save(progetto);

        return progettoMapper.toResponseDTO(progettoDB);

    }

    /**
     * Elimina il progetto desiderato. Solo il proprietario o un ADMIN
     * possono eliminarlo.
     *
     * @param idProgetto id del progetto da eliminare
     * @throws RisorsaNonTrovataException se l'id non corrisponde a nessun progetto esistente
     * @throws it.unipi.makermanagerserver.exception.AccessoNegatoException
     *         se il progetto non appartiene all'utente corrente e non e' ADMIN
     */
    public void elimina(Long idProgetto) {

        ProgettoMaker progetto = cercaProgettoDaId(idProgetto);
        utenteCorrente.verificaProprietarioOAdmin(
            progetto.getAutore(),
            "Non puoi eliminare un progetto che non ti appartiene"
        );

        progettoRepo.deleteById(idProgetto);

    }

    /**
     * Aggiunge una nuova riga alla B.O.M. del progetto indicato.
     *
     * Nota: non esiste un RigaBOMRepository dedicato. La relazione tra
     * ProgettoMaker e RigaBOM e' gestita interamente tramite il cascade
     * definito su BOM.righeFabbisogno (cascade = CascadeType.ALL): per
     * questo motivo la nuova riga viene persistita salvando il progetto
     * "padre", non la riga da sola.
     *
     * @param idProgetto id del progetto a cui aggiungere la riga
     * @param dtoRichiesta dati della riga da aggiungere (id elemento catalogo, quantita)
     * @return il DTO della riga appena creata (comprensivo dell'id generato dal DB)
     * @throws RisorsaNonTrovataException se il progetto non esiste
     * @throws it.unipi.makermanagerserver.exception.AccessoNegatoException
     *         se il progetto non appartiene all'utente corrente e non e' ADMIN
     */
    public RigaBOMResponseDTO aggiungiRigaBOM(Long idProgetto, RigaBOMRequestDTO dtoRichiesta) {

        ProgettoMaker progetto = cercaProgettoDaId(idProgetto);
        utenteCorrente.verificaProprietarioOAdmin(
            progetto.getAutore(),
            "Non puoi modificare la BOM di un progetto che non ti appartiene"
        );

        RigaBOM riga = progettoMapper.toRigaBOM(dtoRichiesta);
        progetto.getDistintaBase().aggiungiRiga(riga);

        /*
         * progettoRepo.save(...) su un ProgettoMaker gia' esistente esegue
         * un merge(): l'entita' restituita e' una copia gestita distinta da
         * "progetto"/"riga" (creati sopra da entita' detached), ed e' SOLO
         * su questa copia che Hibernate popola l'id generato dal DB per la
         * nuova riga. Per questo non possiamo restituire il DTO a partire
         * da "riga", ma dobbiamo recuperare la riga appena aggiunta dalla
         * BOM dell'entita' restituita da save() (e' sempre l'ultima, visto
         * che e' stata appena aggiunta in coda alla lista).
         */
        ProgettoMaker progettoSalvato = progettoRepo.save(progetto);
        List<RigaBOM> righeSalvate = progettoSalvato.getDistintaBase().getRigheFabbisogno();
        RigaBOM rigaSalvata = righeSalvate.get(righeSalvate.size() - 1);

        return progettoMapper.toRigaBOMResponseDTO(rigaSalvata);

    }

    /**
     * Elimina una riga dalla B.O.M. del progetto indicato.
     *
     * L'eliminazione avviene rimuovendo la riga dalla lista Java e salvando
     * di nuovo il progetto: orphanRemoval = true (vedi BOM.java) fa si' che
     * Hibernate cancelli la riga anche fisicamente dal DB, senza bisogno di
     * chiamare alcuna delete esplicita su di essa.
     *
     * @param idProgetto id del progetto proprietario della riga
     * @param idRiga id della riga da eliminare
     * @throws RisorsaNonTrovataException se il progetto non esiste, o se la riga
     *         non esiste o non appartiene al progetto indicato
     * @throws it.unipi.makermanagerserver.exception.AccessoNegatoException
     *         se il progetto non appartiene all'utente corrente e non e' ADMIN
     */
    public void eliminaRigaBOM(Long idProgetto, Long idRiga) {

        ProgettoMaker progetto = cercaProgettoDaId(idProgetto);
        utenteCorrente.verificaProprietarioOAdmin(
            progetto.getAutore(),
            "Non puoi modificare la BOM di un progetto che non ti appartiene"
        );

        BOM bom = progetto.getDistintaBase();

        RigaBOM riga = bom.getRigheFabbisogno()
                .stream()
                .filter(r -> r.getId().equals(idRiga))
                .findFirst()
                .orElseThrow(() -> new RisorsaNonTrovataException(
                    "Riga BOM con id " + idRiga + " non trovata nel progetto " + idProgetto
                ));

        bom.rimuoviRiga(riga);
        progettoRepo.save(progetto);

    }

    // ### Utility private ###

    private ProgettoMaker cercaProgettoDaId(Long idProgetto) {

        return progettoRepo.findById(idProgetto)
                .orElseThrow(() -> new RisorsaNonTrovataException(
                    "ProgettoMaker con id " + idProgetto + " non trovato"
                ));

    }
    
}