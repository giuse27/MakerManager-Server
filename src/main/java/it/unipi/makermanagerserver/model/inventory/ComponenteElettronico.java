package it.unipi.makermanagerserver.model.inventory;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Sottoclasse di ArticoloInventario per componenti elettronici
 * (es. resistenze, microcontrollori, sensori).
 * Campi tecnici specifici (voltaggio, corrente, ecc.) verranno rifiniti
 * in seguito: per ora e' uno scheletro minimo, coerente con la fase v0.0.0.
 */
@Entity
@DiscriminatorValue("COMPONENTE_ELETTRONICO")
public class ComponenteElettronico extends ArticoloInventario {

    public ComponenteElettronico() {
        super();
    }

    public ComponenteElettronico(ElementoCatalogo elementoCatalogo, Inventario inventario, int quantita) {
        super(elementoCatalogo, inventario, quantita);
    }
    
}