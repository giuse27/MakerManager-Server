package it.unipi.makermanagerserver.service.endpoint;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import it.unipi.makermanagerserver.dto.progetto.ProgettoConBomResponseDTO;
import it.unipi.makermanagerserver.dto.progetto.ProgettoRequestDTO;
import it.unipi.makermanagerserver.dto.progetto.ProgettoResponseDTO;
import it.unipi.makermanagerserver.dto.progetto.RigaBOMRequestDTO;
import it.unipi.makermanagerserver.dto.progetto.RigaBOMResponseDTO;
import it.unipi.makermanagerserver.enums.TipologiaProgetto;
import it.unipi.makermanagerserver.exception.DatiNonValidiException;
import it.unipi.makermanagerserver.mapper.ProgettoMapper;
import it.unipi.makermanagerserver.model.project.BOM;
import it.unipi.makermanagerserver.model.project.ProgettoMaker;
import it.unipi.makermanagerserver.model.project.RigaBOM;
import it.unipi.makermanagerserver.repository.ProgettoMakerRepository;
 
/**
 * Fornisce i metodi per l'endpoint /api/progetti.
 * Il controller usa questa classe come intermediaria con repository e mapper.
 */

@Service
public class ProgettoService {

    private final ProgettoMakerRepository progettoRepo;
    private final ProgettoMapper progettoMapper;

    public ProgettoService(
        ProgettoMakerRepository progettoRepo,
        ProgettoMapper progettoMapper
    ) {
 
        this.progettoRepo = progettoRepo;
        this.progettoMapper = progettoMapper;
 
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
     * @throws NoSuchElementException se l'id non corrisponde a nessun progetto esistente
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
     * 
     * @param dtoRichiesta body del post con le caratteristiche del progetto
     * @return Restituisce il dto di risposta del progetto creato
     */
    public ProgettoResponseDTO crea(ProgettoRequestDTO dtoRichiesta) {

        ProgettoMaker progetto = progettoMapper.toProgetto(dtoRichiesta);
        ProgettoMaker progettoDB = progettoRepo.save(progetto);

        return progettoMapper.toResponseDTO(progettoDB);

    }

    /**
     * Elimina il progetto desiderato
     * 
     * @param idProgetto id del progetto da eliminare
     * @throws NoSuchElementException se l'id non corrisponde a nessun progetto esistente
     */
    public void elimina(Long idProgetto) {

        if (!progettoRepo.existsById(idProgetto)) {

            throw new NoSuchElementException(
                "ProgettoMaker con id " + idProgetto + " non trovato"
            );

        }

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
     * @throws NoSuchElementException se il progetto non esiste
     */
    public RigaBOMResponseDTO aggiungiRigaBOM(Long idProgetto, RigaBOMRequestDTO dtoRichiesta) {

        ProgettoMaker progetto = cercaProgettoDaId(idProgetto);

        RigaBOM riga = progettoMapper.toRigaBOM(dtoRichiesta);
        progetto.getDistintaBase().aggiungiRiga(riga);

        progettoRepo.save(progetto);

        return progettoMapper.toRigaBOMResponseDTO(riga);

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
     * @throws NoSuchElementException se il progetto non esiste, o se la riga
     *         non esiste o non appartiene al progetto indicato
     */
    public void eliminaRigaBOM(Long idProgetto, Long idRiga) {

        ProgettoMaker progetto = cercaProgettoDaId(idProgetto);
        BOM bom = progetto.getDistintaBase();

        RigaBOM riga = bom.getRigheFabbisogno()
                .stream()
                .filter(r -> r.getId().equals(idRiga))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                    "Riga BOM con id " + idRiga + " non trovata nel progetto " + idProgetto
                ));

        bom.rimuoviRiga(riga);
        progettoRepo.save(progetto);

    }

    // ### Utility private ###

    private ProgettoMaker cercaProgettoDaId(Long idProgetto) {

        return progettoRepo.findById(idProgetto)
                .orElseThrow(() -> new NoSuchElementException(
                    "ProgettoMaker con id " + idProgetto + " non trovato"
                ));

    }
    
}