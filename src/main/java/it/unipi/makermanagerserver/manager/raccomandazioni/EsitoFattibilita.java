package it.unipi.makermanagerserver.manager.raccomandazioni;

import java.util.List;

/**
 * Esito del calcolo di fattibilità di un progetto rispetto alle disponibilità
 * di un utente. Ho usato un record perché immutabile e si auto inizializza.
 *
 * @param righeTotali numero di elementi distinti richiesti dalla BOM
 * @param dettaglioMancanti elementi non coperti (vuota se il progetto è realizzabile)
 */
public record EsitoFattibilita(int righeTotali, List<ElementoMancante> dettaglioMancanti) {

    /**
     * @return numero di elementi distinti non coperti dall'inventario.
     */
    public int righeMancanti() {
        return dettaglioMancanti.size();
    }

    /**
     * @return numero di elementi distinti gia' interamente coperti.
     */
    public int righeSoddisfatte() {
        return righeTotali - righeMancanti();
    }

    /**
     * @return true se non manca nessun elemento (progetto costruibile subito).
     */
    public boolean realizzabile() {
        return righeMancanti() == 0;
    }

    /**
     * Indice di fattibilita' in [0,1] usato per ordinare i progetti prima
     * dell'invio: frazione di elementi distinti gia' coperti. Una BOM senza
     * righe (caso limite) e' considerata pienamente fattibile (1.0).
     */
    public double indiceFattibilita() {
        if (righeTotali == 0) {
            return 1.0;           
        }
        return (double) righeSoddisfatte() / righeTotali;
    }

    /**
     * @return somma dei pezzi mancanti su tutti gli elementi non coperti.
     *         Usato solo come criterio di ordinamento a parita' di tutto il resto.
     */
    public int pezziMancantiTotali() {
        return dettaglioMancanti
                .stream()
                .mapToInt(ElementoMancante::quantitaMancante)
                .sum();
    }

}