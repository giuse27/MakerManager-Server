package it.unipi.makermanagerserver.dto.common;

/**
 * DTO condiviso comune a ArticoloInventario e RigaBOM per l'aggiornamento della 
 * quantità di un articolo in Inventario oppure per l'aggiornamento di una 
 * RigaBOM di una BOM all'interno di ProgettoMaker
 */
public class AggiornaQuantitaDTO {

    private int quantita;

    public AggiornaQuantitaDTO() {}

    public AggiornaQuantitaDTO(int quantita) {
        this.quantita = quantita;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

}