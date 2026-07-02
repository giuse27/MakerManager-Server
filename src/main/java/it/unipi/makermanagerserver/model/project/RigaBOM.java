package it.unipi.makermanagerserver.model.project;

import it.unipi.makermanagerserver.model.catalog.ElementoCatalogo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * --- W.I.P. ---
 * 
 * Implementazione temporanea
 */
@Entity
@Table(name = "righe_bom")
public class RigaBOM {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Molte righe possono puntare allo stesso elemento del catalogo
    @ManyToOne
    @JoinColumn(name = "elemento_catalogo_id", nullable = false)
    private ElementoCatalogo articoloRichiesto;

    @Column(name = "quantita_richiesta")
    private int quantitaRichiesta;

    // Costruttori

    public RigaBOM() {
    }

    public RigaBOM(ElementoCatalogo articoloRichiesto, int quantitaRichiesta) {
        this.articoloRichiesto = articoloRichiesto;
        this.quantitaRichiesta = quantitaRichiesta;
    }

    // Getter e Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ElementoCatalogo getArticoloRichiesto() {
        return articoloRichiesto;
    }

    public void setArticoloRichiesto(ElementoCatalogo articoloRichiesto) {
        this.articoloRichiesto = articoloRichiesto;
    }

    public int getQuantitaRichiesta() {
        return quantitaRichiesta;
    }

    public void setQuantitaRichiesta(int quantitaRichiesta) {
        this.quantitaRichiesta = quantitaRichiesta;
    }

}