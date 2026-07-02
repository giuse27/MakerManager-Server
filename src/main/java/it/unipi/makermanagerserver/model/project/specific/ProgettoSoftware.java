package it.unipi.makermanagerserver.model.project.specific;

import it.unipi.makermanagerserver.model.project.ProgettoMaker;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * --- W.I.P. ---
 */
@Entity
@DiscriminatorValue("SOFTWARE")
public class ProgettoSoftware extends ProgettoMaker {

    private String linguaggioPrevalente;
    private String piattaformaTarget;

    public ProgettoSoftware() {
        super();
    }

    // --- Getter e Setter ---

    public String getLinguaggioPrevalente() {
        return linguaggioPrevalente;
    }

    public void setLinguaggioPrevalente(String linguaggioPrevalente) {
        this.linguaggioPrevalente = linguaggioPrevalente;
    }

    public String getPiattaformaTarget() {
        return piattaformaTarget;
    }

    public void setPiattaformaTarget(String piattaformaTarget) {
        this.piattaformaTarget = piattaformaTarget;
    }
    
}