package it.unipi.makermanagerserver.dto.progetto;

import java.util.List;

/**
 * DTO di RISPOSTA "completo": usato solo da GET /api/progetti/{id}, quando
 * il client ha bisogno di tutte le informazioni di un singolo progetto,
 * inclusa la sua B.O.M.
 */
public class ProgettoConBomResponseDTO {

    private Long id;
    private String tipo;
    private String nome;
    private String descrizione;
    private List<RigaBOMResponseDTO> bom;

    public ProgettoConBomResponseDTO() {
    }

    public ProgettoConBomResponseDTO(Long id, String tipo, String nome, String descrizione, List<RigaBOMResponseDTO> bom) {
        this.id = id;
        this.tipo = tipo;
        this.nome = nome;
        this.descrizione = descrizione;
        this.bom = bom;
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

    public List<RigaBOMResponseDTO> getBom() {
        return bom;
    }

    public void setBom(List<RigaBOMResponseDTO> bom) {
        this.bom = bom;
    }

}