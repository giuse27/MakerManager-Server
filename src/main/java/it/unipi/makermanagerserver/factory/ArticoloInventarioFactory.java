package it.unipi.makermanagerserver.factory;

import it.unipi.makermanagerserver.enums.TipologiaElemento;
import it.unipi.makermanagerserver.exception.DatiNonValidiException;
import it.unipi.makermanagerserver.model.catalog.ElementoCatalogo;
import it.unipi.makermanagerserver.model.inventory.ArticoloInventario;
import it.unipi.makermanagerserver.model.inventory.Inventario;
import it.unipi.makermanagerserver.model.inventory.specific.ComponenteElettronico;
import it.unipi.makermanagerserver.model.inventory.specific.MaterialeConsumabile;

/**
 * ATTENZIONE
 * Aggiorna questa classe quando fai modifiche a TipologiaElemento
 */
public class ArticoloInventarioFactory {

    // Costruttore privato: essendo una classe di utilità non voglio che qualcuno
    // ne possa creare un'istanza, e invece userò metodi statici
    private ArticoloInventarioFactory() {
        throw new UnsupportedOperationException(
            "Questa è una classe factory di utilità e non può essere istanziata."
        );
    }

    /**
     * Factory per istanziare la sottoclasse corretta di ArticoloInventario
     * sulla base del campo "tipo" proveniente dal JSON.
     * 
     * @throws DatiNonValidiException se "tipo" non corrisponde a nessun
     *         valore di TipologiaElemento, o corrisponde a una tipologia
     *         non ancora supportata da questa factory
     */
    public static ArticoloInventario creaArticoloInventario(
            String tipo,
            ElementoCatalogo elemento,
            Inventario inventario,
            int quantita
    ) {

        TipologiaElemento tipologia;
        try {
            tipologia = TipologiaElemento.valueOf(tipo);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new DatiNonValidiException(
                "Tipologia di articolo '" + tipo + "' non valida. Valori ammessi: "
                + java.util.Arrays.toString(TipologiaElemento.values())
            );
        }

        return switch (tipologia) {

            case COMPONENTE_ELETTRONICO -> new ComponenteElettronico(elemento, inventario, quantita);
            case MATERIALE_CONSUMABILE -> new MaterialeConsumabile(elemento, inventario, quantita);
            
            default -> throw new DatiNonValidiException(
                "Tipologia di articolo non ancora supportata: " + tipo 
            );
            
        };

    }

}