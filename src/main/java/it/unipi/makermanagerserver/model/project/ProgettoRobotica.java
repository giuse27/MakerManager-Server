package it.unipi.makermanagerserver.model.project;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * --- W.I.P. ---
 */
@Entity
@DiscriminatorValue("ROBOTICA")
public class ProgettoRobotica extends ProgettoMaker {

    private String firmwareRepositoryUrl;
    private int numeroGradiDiLiberta;

    public ProgettoRobotica() {
        super();
    }

    // --- Getter e Setter ---

    public String getFirmwareRepositoryUrl() {
        return firmwareRepositoryUrl;
    }

    public void setFirmwareRepositoryUrl(String firmwareRepositoryUrl) {
        this.firmwareRepositoryUrl = firmwareRepositoryUrl;
    }

    public int getNumeroGradiDiLiberta() {
        return numeroGradiDiLiberta;
    }

    public void setNumeroGradiDiLiberta(int numeroGradiDiLiberta) {
        this.numeroGradiDiLiberta = numeroGradiDiLiberta;
    }
    
}