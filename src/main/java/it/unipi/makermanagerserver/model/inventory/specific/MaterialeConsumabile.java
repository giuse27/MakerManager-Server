package it.unipi.makermanagerserver.model.inventory.specific;

import it.unipi.makermanagerserver.model.catalog.ElementoCatalogo;
import it.unipi.makermanagerserver.model.inventory.ArticoloInventario;
import it.unipi.makermanagerserver.model.inventory.Inventario;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Sottoclasse di ArticoloInventario per materiali consumabili
 * (es. bobine di filamento PLA, resina, colla).
 * Campi tecnici specifici (stato di usura, scadenza, ecc.) verranno rifiniti
 * in seguito: per ora e' uno scheletro minimo, coerente con la fase v0.0.0.
 */
@Entity
@DiscriminatorValue("MATERIALE_CONSUMABILE")
public class MaterialeConsumabile extends ArticoloInventario {

    public MaterialeConsumabile() {
        super();
    }

    public MaterialeConsumabile(ElementoCatalogo elementoCatalogo, Inventario inventario, int quantita) {
        super(elementoCatalogo, inventario, quantita);
    }
    
}