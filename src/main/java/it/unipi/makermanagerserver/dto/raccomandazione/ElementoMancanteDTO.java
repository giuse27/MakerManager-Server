package it.unipi.makermanagerserver.dto.raccomandazione;

/**
 * singolo elemento che manca per la realizzazione di un progetto 
 * l'elemento catalogo è associato con una stringa
 */
public class ElementoMancanteDTO {

    private String elementoCatalogo;
    private int quantitaRichiesta;
    private int quantitaPosseduta;
    private int quantitaMancante;

    public ElementoMancanteDTO() {
    }

    public ElementoMancanteDTO(
        String elementoCatalogo, 
        int quantitaRichiesta,
        int quantitaPosseduta, 
        int quantitaMancante
    ) {
        this.elementoCatalogo = elementoCatalogo;
        this.quantitaRichiesta = quantitaRichiesta;
        this.quantitaPosseduta = quantitaPosseduta;
        this.quantitaMancante = quantitaMancante;
    }

    public String getElementoCatalogo() {
        return elementoCatalogo;
    }

    public void setElementoCatalogo(String elementoCatalogo) {
        this.elementoCatalogo = elementoCatalogo;
    }

    public int getQuantitaRichiesta() {
        return quantitaRichiesta;
    }

    public void setQuantitaRichiesta(int quantitaRichiesta) {
        this.quantitaRichiesta = quantitaRichiesta;
    }

    public int getQuantitaPosseduta() {
        return quantitaPosseduta;
    }

    public void setQuantitaPosseduta(int quantitaPosseduta) {
        this.quantitaPosseduta = quantitaPosseduta;
    }

    public int getQuantitaMancante() {
        return quantitaMancante;
    }

    public void setQuantitaMancante(int quantitaMancante) {
        this.quantitaMancante = quantitaMancante;
    }

}