package it.unipi.makermanagerserver.model.catalog;

import it.unipi.makermanagerserver.enums.TipologiaElemento;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 
 * ElementoCatalogo
 * @author giuse27
 */
@Entity
@Table(name = "catalogo_elementi")
public class ElementoCatalogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descrizione;

    @Enumerated(EnumType.STRING)
    private TipologiaElemento tipologia;

    /**
     * Costruttore vuoto richiesto da JPA/Hibernate per la ricostruzione delle entita'
     */
    public ElementoCatalogo() {
    }

    /**
     * Costruttore completo di tutti i parametri per definire un ElementoCatalogo
     * 
     * @param nome - nome dell'elemento
     * @param descrizione - descrizione dell'elemento
     * @param tipologia - tipologa data da TipologiaElemento per l'elemento
     */
    public ElementoCatalogo(String nome, String descrizione, TipologiaElemento tipologia) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.tipologia = tipologia;
    }

    // Getter e Setter per permettere a JPA di accedere ai campi privati
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public TipologiaElemento getTipologia() {
        return tipologia;
    }

    public void setTipologia(TipologiaElemento tipologia) {
        this.tipologia = tipologia;
    }

}
