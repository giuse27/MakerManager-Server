package it.unipi.makermanagerserver.model.inventory.specific;

import it.unipi.makermanagerserver.model.catalog.ElementoCatalogo;
import it.unipi.makermanagerserver.model.inventory.ArticoloInventario;
import it.unipi.makermanagerserver.model.inventory.Inventario;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Sottoclasse di ArticoloInventario per software o licenze
 * Campi tecnici specifici verranno rifiniti
 * in seguito: per ora e' uno scheletro minimo
 */
@Entity
@DiscriminatorValue("SOFTWARE")
public class Software extends ArticoloInventario {

    public Software() {
        super();
    }

    public Software(ElementoCatalogo elementoCatalogo, Inventario inventario, int quantita) {
        super(elementoCatalogo, inventario, quantita);
    }
    
}