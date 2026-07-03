package it.unipi.makermanagerserver.dto.inizializza;

/**
 * DTO "di caricamento" per una voce dell'array "bom" annidato dentro
 * ogni progetto nel JSON di inizializzazione. Vedi ElementoCatalogoInitDTO
 * per la spiegazione generale del ruolo di questi DTO.
 *
 * Come per ArticoloInventarioInitDTO, elementoCatalogo e' un NOME da
 * risolvere nel service, non l'entity gia' pronta.
 */
public class RigaBOMInitDTO {

    private String elementoCatalogo;
    private int quantita;

    public RigaBOMInitDTO() {

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
