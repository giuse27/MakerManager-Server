package it.unipi.makermanagerserver.model.project;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * --- W.I.P. ---
 */
@Entity
@DiscriminatorValue("ELETTRONICA")
public class ProgettoElettronica extends ProgettoMaker {

    private String schemaElettricoUrl;
    private boolean richiedePcbCustom;

    public ProgettoElettronica() {
        super();
    }

    // --- Getter e Setter ---

    public String getSchemaElettricoUrl() {
        return schemaElettricoUrl;
    }

    public void setSchemaElettricoUrl(String schemaElettricoUrl) {
        this.schemaElettricoUrl = schemaElettricoUrl;
    }

    public boolean isRichiedePcbCustom() {
        return richiedePcbCustom;
    }

    public void setRichiedePcbCustom(boolean richiedePcbCustom) {
        this.richiedePcbCustom = richiedePcbCustom;
    }

}