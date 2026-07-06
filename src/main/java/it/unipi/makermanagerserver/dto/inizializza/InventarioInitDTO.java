package it.unipi.makermanagerserver.dto.inizializza;

/**
 * DTO "di caricamento" per una voce dell'array "inventari" nel JSON
 * di inizializzazione. Vedi ElementoCatalogoInitDTO per la spiegazione
 * generale del ruolo di questi DTO.
 */
public class InventarioInitDTO {

    private String nome;
    private String utente;

    public InventarioInitDTO() {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUtente() {
        return utente;
    }

    public void setUtente(String utente) {
        this.utente = utente;
    }
    
}
