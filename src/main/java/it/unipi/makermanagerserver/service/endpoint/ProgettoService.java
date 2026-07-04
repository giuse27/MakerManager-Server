package it.unipi.makermanagerserver.service.endpoint;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import it.unipi.makermanagerserver.dto.progetto.ProgettoRequestDTO;
import it.unipi.makermanagerserver.dto.progetto.ProgettoResponseDTO;
import it.unipi.makermanagerserver.enums.TipologiaProgetto;
import it.unipi.makermanagerserver.mapper.ProgettoMapper;
import it.unipi.makermanagerserver.model.project.ProgettoMaker;
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
     * @return Restituisce tutti i progetti dto in catalogo
     */
    public List<ProgettoResponseDTO> trovaTutti() {
        
        return progettoRepo.findAll()
                .stream()
                .map(progettoMapper::toResponseDTO)
                .toList();

    }

    /**
     * 
     * @param idProgetto id del progetto da visualizzare
     * @return Restituisce il progetto dto contrassegnato da idProgetto
     */
    public ProgettoResponseDTO trovaPerId(Long idProgetto) {

        ProgettoMaker progetto = progettoRepo.findById(idProgetto)
                .orElseThrow(() -> new NoSuchElementException(
                    "ProgettoMaker con id " + idProgetto + " non trovato"
                ));
 
        return progettoMapper.toResponseDTO(progetto);

    }

    /**
     * 
     * @param tipologia tipologia di progetti da cercare
     * @return restituisce tutti i progetti appartenenti a una certa tipologia
     */
    public List<ProgettoResponseDTO> trovaPerTipologia(String tipologia) {

        return progettoRepo.findByTipologia(TipologiaProgetto.valueOf(tipologia))
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
     */
    public void elimina(Long idProgetto) {

        if (!progettoRepo.existsById(idProgetto)) {

            throw new NoSuchElementException(
                "ProgettoMaker con id " + idProgetto + " non trovato"
            );

        }

        progettoRepo.deleteById(idProgetto);

    }
    
}
