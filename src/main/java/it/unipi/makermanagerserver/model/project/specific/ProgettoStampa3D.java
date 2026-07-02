package it.unipi.makermanagerserver.model.project.specific;

import it.unipi.makermanagerserver.model.project.ProgettoMaker;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * --- W.I.P. ---
 */
@Entity
@DiscriminatorValue("STAMPA_3D")
public class ProgettoStampa3D extends ProgettoMaker {

    private String fileStlUrl;
    private int tempoStampaMinuti;
    private double pesoStimatoGrammi;

    public ProgettoStampa3D() {
        super();
    }

    // --- Getter e Setter ---

    public String getFileStlUrl() {
        return fileStlUrl;
    }

    public void setFileStlUrl(String fileStlUrl) {
        this.fileStlUrl = fileStlUrl;
    }

    public int getTempoStampaMinuti() {
        return tempoStampaMinuti;
    }

    public void setTempoStampaMinuti(int tempoStampaMinuti) {
        this.tempoStampaMinuti = tempoStampaMinuti;
    }

    public double getPesoStimatoGrammi() {
        return pesoStimatoGrammi;
    }

    public void setPesoStimatoGrammi(double pesoStimatoGrammi) {
        this.pesoStimatoGrammi = pesoStimatoGrammi;
    }

}