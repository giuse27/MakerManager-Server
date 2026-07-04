package it.unipi.makermanagerserver.dto.progetto;

/**
 * DTO di RISPOSTA: dati restituiti al client per un ProgettoMaker
 */
public class ProgettoResponseDTO {

    private Long id;
    private String tipo;
    private String nome;
    private String descrizione;

    public ProgettoResponseDTO() {
    }

    public ProgettoResponseDTO(Long id, String tipo, String nome, String descrizione) {
        this.id = id;
        this.tipo = tipo;
        this.nome = nome;
        this.descrizione = descrizione;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

}