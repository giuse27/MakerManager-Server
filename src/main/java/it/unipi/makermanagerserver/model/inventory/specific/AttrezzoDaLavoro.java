package it.unipi.makermanagerserver.model.inventory.specific;

import it.unipi.makermanagerserver.model.catalog.ElementoCatalogo;
import it.unipi.makermanagerserver.model.inventory.ArticoloInventario;
import it.unipi.makermanagerserver.model.inventory.Inventario;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Sottoclasse di ArticoloInventario per attrezzi o strumenti
 * Campi tecnici specifici verranno rifiniti
 * in seguito: per ora e' uno scheletro minimo
 */
@Entity
@DiscriminatorValue("ATTREZZO_DA_LAVORO")
public class AttrezzoDaLavoro extends ArticoloInventario {

    public AttrezzoDaLavoro() {
        super();
    }

    public AttrezzoDaLavoro(ElementoCatalogo elementoCatalogo, Inventario inventario, int quantita) {
        super(elementoCatalogo, inventario, quantita);
    }
    
}