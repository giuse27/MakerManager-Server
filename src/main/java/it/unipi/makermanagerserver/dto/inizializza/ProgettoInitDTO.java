package it.unipi.makermanagerserver.dto.inizializza;

import java.util.List;

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

    private String tipo;
    private String nome;
    private String descrizione;
    private List<RigaBOMInitDTO> bom;

    public ProgettoInitDTO() {

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

    public List<RigaBOMInitDTO> getBom() {
        return bom;
    }

    public void setBom(List<RigaBOMInitDTO> bom) {
        this.bom = bom;
    }
    
}
