package it.unipi.makermanagerserver.dto.init;

/**
 * DTO "di caricamento" per una voce dell'array "progetti" nel JSON di
 * inizializzazione. Vedi ElementoCatalogoInitDTO per la spiegazione
 * generale del ruolo di questi DTO.
 *
 * "tipo" corrisponde al discriminator di ProgettoMaker (STAMPA_3D,
 * ELETTRONICA, ROBOTICA, SOFTWARE): il service userà questo valore per
 * decidere quale sottoclasse istanziare.
 */
public class ProgettoInitDTO {
    
}
