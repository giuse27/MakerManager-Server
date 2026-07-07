package it.unipi.makermanagerserver.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import it.unipi.makermanagerserver.dto.raccomandazione.ElementoMancanteDTO;
import it.unipi.makermanagerserver.dto.raccomandazione.ProgettoConsigliatoResponseDTO;
import it.unipi.makermanagerserver.manager.raccomandazioni.ElementoMancante;
import it.unipi.makermanagerserver.manager.raccomandazioni.EsitoFattibilita;
import it.unipi.makermanagerserver.model.project.ProgettoMaker;
import it.unipi.makermanagerserver.model.user.Utente;

/**
 * Converte la coppia (ProgettoMaker + EsitoFattibilita) nel DTO di risposta
 * per le raccomandazioni.
 */
@Component
public class ProgettiConsigliatiMapper {

    public ProgettoConsigliatoResponseDTO toConsigliatoDTO(
        ProgettoMaker progetto, EsitoFattibilita esito
    ) {

        List<ElementoMancanteDTO> dettaglio = esito.dettaglioMancanti()
                .stream()
                .map(this::toElementoMancanteDTO)
                .toList();

        /*
            IMPORTANTE: l'autore l'unico modo che ha per essere null è tramite
            /inizializza se viene impostato male il json. Ma visto che mi fido
            di me stesso evito i controlli su null per leggibilità
         */
        Utente autore = progetto.getAutore();

        return new ProgettoConsigliatoResponseDTO(
                progetto.getId(),
                progetto.getTipologia().name(),
                progetto.getNome(),
                progetto.getDescrizione(),
                autore.getId(),
                autore.getNickname(),
                arrotonda(esito.indiceFattibilita()),
                esito.realizzabile(),
                esito.righeTotali(),
                esito.righeMancanti(),
                dettaglio
        );

    }

    // ### Utility private ###

    private ElementoMancanteDTO toElementoMancanteDTO(ElementoMancante mancante) {

        return new ElementoMancanteDTO(
                mancante.nomeElemento(),
                mancante.quantitaRichiesta(),
                mancante.quantitaPosseduta(),
                mancante.quantitaMancante()
        );

    }

    /**
     * Arrotonda l'indice a 3 cifre decimali, per un payload piu' pulito
     * (es. 0.667 invece di 0.6666666666666666).
     */
    private double arrotonda(double valore) {
        return Math.round(valore * 1000.0) / 1000.0;
    }

}