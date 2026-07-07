package it.unipi.makermanagerserver.dto.common;

import jakarta.validation.constraints.Min;

/**
 * DTO condiviso comune a ArticoloInventario e RigaBOM per l'aggiornamento della 
 * quantità di un articolo in Inventario oppure per l'aggiornamento di una 
 * RigaBOM di una BOM all'interno di ProgettoMaker
 */
public class AggiornaQuantitaDTO {

    @Min(value = 0, message = "Non puoi impostare una quantità negativa")
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