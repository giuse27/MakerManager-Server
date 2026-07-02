package it.unipi.makermanagerserver.model.inventory;

import it.unipi.makermanagerserver.model.catalog.ElementoCatalogo;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Classe astratta base per il pattern Type-Instance: rappresenta l'istanza
 * fisica posseduta di un ElementoCatalogo, con le informazioni comuni a
 * qualsiasi tipo di articolo (quantita', appartenenza a un Inventario).
 *
 * Le sottoclassi (ComponenteElettronico, MaterialeConsumabile, ...) aggiungono
 * i campi tecnici specifici. Tutte condividono la stessa tabella "inventario_articoli"
 * grazie alla strategia SINGLE_TABLE: e' la scelta piu' semplice e performante
 * (nessun JOIN in lettura), a costo di colonne nullable per i campi non comuni.
 */
@Entity
@Table(name = "inventario_articoli")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_articolo", discriminatorType = DiscriminatorType.STRING)
public abstract class ArticoloInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Lato "proprietario" della relazione con ElementoCatalogo: questa e' la
     * FK reale nel DB (colonna elemento_catalogo_id). Piu' righe di
     * ArticoloInventario possono riferire lo stesso ElementoCatalogo
     * (es. due bobine PLA distinte con usura diversa, stesso elemento di catalogo).
     */
    @ManyToOne
    @JoinColumn(name = "elemento_catalogo_id")
    private ElementoCatalogo elementoCatalogo;

    /*
     * Lato "proprietario" della relazione con Inventario: FK reale nel DB
     * (colonna inventario_id). E' il campo referenziato da mappedBy in
     * Inventario.articoli.
     */
    @ManyToOne
    @JoinColumn(name = "inventario_id")
    private Inventario inventario;

    private int quantita;

    // COSTRUTTORI

    protected ArticoloInventario() {
    }

    protected ArticoloInventario(ElementoCatalogo elementoCatalogo, Inventario inventario, int quantita) {
        this.elementoCatalogo = elementoCatalogo;
        this.inventario = inventario;
        this.quantita = quantita;
    }

    // GETTER E SETTER

    public Long getId() {
        return id;
    }

    public ElementoCatalogo getElementoCatalogo() {
        return elementoCatalogo;
    }

    public void setElementoCatalogo(ElementoCatalogo elementoCatalogo) {
        this.elementoCatalogo = elementoCatalogo;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }
}