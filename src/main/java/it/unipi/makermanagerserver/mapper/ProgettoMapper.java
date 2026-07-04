package it.unipi.makermanagerserver.mapper;

import org.springframework.stereotype.Component;

import it.unipi.makermanagerserver.dto.progetto.ProgettoRequestDTO;
import it.unipi.makermanagerserver.dto.progetto.ProgettoResponseDTO;
import it.unipi.makermanagerserver.enums.TipologiaElemento;
import it.unipi.makermanagerserver.enums.TipologiaProgetto;
import it.unipi.makermanagerserver.exception.DatiNonValidiException;
import it.unipi.makermanagerserver.factory.ProgettoMakerFactory;
import it.unipi.makermanagerserver.model.project.ProgettoMaker;

@Component
public class ProgettoMapper {

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
     * Converte la stringa tipologia in un valore dell'enum TipologiaElemento,
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
                + java.util.Arrays.toString(TipologiaElemento.values())
            );

        }

    }
    
}
