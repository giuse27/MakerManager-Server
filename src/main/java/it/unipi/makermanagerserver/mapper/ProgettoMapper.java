package it.unipi.makermanagerserver.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import it.unipi.makermanagerserver.dto.progetto.ProgettoConBomResponseDTO;
import it.unipi.makermanagerserver.dto.progetto.ProgettoRequestDTO;
import it.unipi.makermanagerserver.dto.progetto.ProgettoResponseDTO;
import it.unipi.makermanagerserver.dto.progetto.RigaBOMRequestDTO;
import it.unipi.makermanagerserver.dto.progetto.RigaBOMResponseDTO;
import it.unipi.makermanagerserver.enums.TipologiaProgetto;
import it.unipi.makermanagerserver.exception.DatiNonValidiException;
import it.unipi.makermanagerserver.exception.RisorsaNonTrovataException;
import it.unipi.makermanagerserver.factory.ProgettoMakerFactory;
import it.unipi.makermanagerserver.model.catalog.ElementoCatalogo;
import it.unipi.makermanagerserver.model.project.ProgettoMaker;
import it.unipi.makermanagerserver.model.project.RigaBOM;
import it.unipi.makermanagerserver.repository.ElementoCatalogoRepository;

@Component
public class ProgettoMapper {

    private final ElementoCatalogoRepository elementoCatalogoRepository;

    public ProgettoMapper(ElementoCatalogoRepository elementoCatalogoRepository) {
        this.elementoCatalogoRepository = elementoCatalogoRepository;
    }

    public ProgettoMaker toProgetto(ProgettoRequestDTO dto) {

        TipologiaProgetto tipologia = risolviTipologia(dto.getTipo());

        ProgettoMaker progetto = ProgettoMakerFactory.creaProgetto(
            tipologia
        );

        progetto.setNome(dto.getNome());
        progetto.setDescrizione(dto.getDescrizione());
        progetto.setTipologia(tipologia);

        return progetto;

    }

    public ProgettoResponseDTO toResponseDTO(ProgettoMaker progetto) {

        return new ProgettoResponseDTO(
            progetto.getId(),
            progetto.getTipologia().name(),
            progetto.getNome(),
            progetto.getDescrizione()
        );
    
    }

    /**
     * Converte l'entity in un DTO "completo", comprensivo della lista di
     * righe della B.O.M. Usato solo da GET /api/progetti/{id}.
     */
    public ProgettoConBomResponseDTO toResponseDTOConBom(ProgettoMaker progetto) {

        List<RigaBOMResponseDTO> righeBom = progetto.getDistintaBase()
                .getRigheFabbisogno()
                .stream()
                .map(this::toRigaBOMResponseDTO)
                .toList();

        return new ProgettoConBomResponseDTO(
            progetto.getId(),
            progetto.getTipologia().name(),
            progetto.getNome(),
            progetto.getDescrizione(),
            righeBom
        );

    }

    /**
     * Converte una singola RigaBOM (entity) nel suo DTO di risposta.
     */
    public RigaBOMResponseDTO toRigaBOMResponseDTO(RigaBOM riga) {

        return new RigaBOMResponseDTO(
            riga.getId(),
            riga.getArticoloRichiesto().getNome(),
            riga.getQuantitaRichiesta()
        );

    }

    /**
     * Converte il DTO di richiesta in una nuova RigaBOM, risolvendo l'id
     * dell'ElementoCatalogo indicato dal client.
     *
     * @throws RisorsaNonTrovataException se l'id non corrisponde a nessun
     *         ElementoCatalogo esistente
     */
    public RigaBOM toRigaBOM(RigaBOMRequestDTO dto) {

        ElementoCatalogo elemento = elementoCatalogoRepository.findById(dto.getIdElementoCatalogo())
                .orElseThrow(() -> new RisorsaNonTrovataException(
                        "Impossibile aggiungere la riga alla BOM: Elemento Catalogo non trovato con ID "
                        + dto.getIdElementoCatalogo()
                ));

        return new RigaBOM(elemento, dto.getQuantita());

    }

    /**
     * Converte la stringa tipologia in un valore dell'enum TipologiaProgetto,
     * intercettando un eventuale valore non valido con un messaggio chiaro
     * per il client, invece di lasciare che IllegalArgumentException risalga
     * "nuda" fino al GlobalExceptionHandler.
     */
    private TipologiaProgetto risolviTipologia(String tipologia) {

        try {

            return TipologiaProgetto.valueOf(tipologia);

        } catch (IllegalArgumentException | NullPointerException e) {

            throw new DatiNonValidiException(
                "Tipologia '" + tipologia + "' non valida. Valori ammessi: "
                + java.util.Arrays.toString(TipologiaProgetto.values())
            );

        }

    }
    
}