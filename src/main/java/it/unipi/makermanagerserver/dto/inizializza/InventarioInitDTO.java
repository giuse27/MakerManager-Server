package it.unipi.makermanagerserver.dto.inizializza;

/**
 * DTO "di caricamento" per una voce dell'array "inventari" nel JSON
 * di inizializzazione. Vedi ElementoCatalogoInitDTO per la spiegazione
 * generale del ruolo di questi DTO.
 */
public class InventarioInitDTO {

    private String nome;
    private Long idUtente;

    public InventarioInitDTO() {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(Long idUtente) {
        this.idUtente = idUtente;
    }
    
}
