package it.unipi.makermanagerserver.dto.progetto;

/**
 * DTO di RISPOSTA: rappresenta una singola riga della B.O.M. restituita
 * al client (usato sia come esito di POST sia dentro ProgettoConBomResponseDTO).
 */
public class RigaBOMResponseDTO {

    private Long id;
    private String elementoCatalogo;
    private int quantita;

    public RigaBOMResponseDTO() {
    }

    public RigaBOMResponseDTO(Long id, String elementoCatalogo, int quantita) {
        this.id = id;
        this.elementoCatalogo = elementoCatalogo;
        this.quantita = quantita;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getElementoCatalogo() {
        return elementoCatalogo;
    }

    public void setElementoCatalogo(String elementoCatalogo) {
        this.elementoCatalogo = elementoCatalogo;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

}