package it.unipi.makermanagerserver.model.common;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;

/**
 * --- W.I.P. ---
 * 
 * Questa classe è solo un placeholder temporaneo dovrò implementarla in futuro
 */
@Embeddable
public class Progresso {

    @Column(name = "stato_avanzamento")
    private String statoAvanzamento; // Es: "Ideazione", "In Corso", "Completato"

    @Column(name = "ultimo_aggiornamento")
    private LocalDateTime ultimoAggiornamento;

    @Column(name = "percentuale_completamento")
    private double percentualeCompletamento;

    // Costruttori

    public Progresso() {
        // Valori di default quando crei un nuovo Progresso vuoto
        this.statoAvanzamento = "Ideazione";
        this.percentualeCompletamento = 0.0;
        this.ultimoAggiornamento = LocalDateTime.now();
    }

    public Progresso(String statoAvanzamento, double percentualeCompletamento) {
        this.statoAvanzamento = statoAvanzamento;
        this.percentualeCompletamento = percentualeCompletamento;
        this.ultimoAggiornamento = LocalDateTime.now();
    }

    // Metodo di utilità per la logica di Business
    
    // Invece di usare i setter uno ad uno, uso questo metodo per avanzare il progetto
    public void aggiornaProgresso(String nuovoStato, double nuovaPercentuale) {
        this.statoAvanzamento = nuovoStato;
        this.percentualeCompletamento = nuovaPercentuale;
        this.ultimoAggiornamento = LocalDateTime.now(); // Aggiorna la data in automatico
    }

    // Getter e Setter

    public String getStatoAvanzamento() {
        return statoAvanzamento;
    }

    public void setStatoAvanzamento(String statoAvanzamento) {
        this.statoAvanzamento = statoAvanzamento;
    }

    public LocalDateTime getUltimoAggiornamento() {
        return ultimoAggiornamento;
    }

    public void setUltimoAggiornamento(LocalDateTime ultimoAggiornamento) {
        this.ultimoAggiornamento = ultimoAggiornamento;
    }

    public double getPercentualeCompletamento() {
        return percentualeCompletamento;
    }

    public void setPercentualeCompletamento(double percentualeCompletamento) {
        this.percentualeCompletamento = percentualeCompletamento;
    }

}