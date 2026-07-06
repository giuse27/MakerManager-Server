package it.unipi.makermanagerserver.model.inventory;

import java.util.ArrayList;
import java.util.List;

import it.unipi.makermanagerserver.model.user.Utente;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Un Inventario è un "contenitore" di elementi di tipo ArticoloInventario ed
 * è associato ad un Utente. Un Utente può possedere più Inventario indipendenti.
 *  
 * Inventario
 * @author giuse27
 */
@Entity
@Table(name = "inventario")
public class Inventario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    /**
     * Appartenenza dell'inventario (uno o più) all'utente
     */
    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    /*
     * Lato "inverso" della relazione bidirezionale con ArticoloInventario.
     * mappedBy = "inventario" indica che la chiave esterna reale nel DB
     * (colonna inventario_id) e' gestita dal campo "inventario" presente
     * in ArticoloInventario, non qui: questa lista serve solo per navigare
     * comodamente i dati lato Java.
     *
     * cascade = CascadeType.ALL: operazioni su Inventario (es. eliminazione)
     * si propagano automaticamente ai suoi ArticoloInventario.
     * orphanRemoval = true: se un ArticoloInventario viene rimosso dalla lista,
     * Hibernate lo elimina anche dal DB (non resta "orfano").
     */
    @OneToMany(
        mappedBy = "inventario", 
        cascade = CascadeType.ALL, 
        orphanRemoval = true
    )
    private List<ArticoloInventario> articoli = new ArrayList<>();

    // COSTRUTTORI
    
    public Inventario() {
    }
 
    public Inventario(String nome, Utente utente) {
        this.nome = nome;
        this.utente = utente;
    }

    // GETTER E SETTER
 
    public Long getId() {
        return id;
    }
 
    public String getNome() {
        return nome;
    }
 
    public void setNome(String nome) {
        this.nome = nome;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }
 
    public List<ArticoloInventario> getArticoli() {
        return articoli;
    }
 
    /**
     * Aggiunge un articolo mantenendo coerenti entrambi i lati della relazione
     * bidirezionale (buona pratica JPA: evita disallineamenti tra oggetti Java
     * e stato reale nel DB finche' non viene fatto il flush).
     */
    public void aggiungiArticolo(ArticoloInventario articolo) {
        articoli.add(articolo);
        articolo.setInventario(this);
    }

}
