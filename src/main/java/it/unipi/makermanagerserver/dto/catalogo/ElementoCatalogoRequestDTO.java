package it.unipi.makermanagerserver.dto.catalogo;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO di RICHIESTA: rappresenta i dati che il client invia per creare
 * un nuovo ElementoCatalogo (POST /api/catalogo).
 *
 * Non contiene l'id: non esiste ancora, verra' generato dal database
 * al momento del salvataggio (GenerationType.IDENTITY).
 *
 * Il campo tipologia resta una String (non l'enum TipologiaElemento)
 * per lo stesso motivo visto nei DTO di caricamento: la validazione/
 * conversione avviene nel mapper, cosi' possiamo intercettare un
 * eventuale valore non valido con un errore controllato invece di
 * lasciare che Jackson fallisca prima ancora di entrare nel controller.
 */
public class ElementoCatalogoRequestDTO {

    @NotBlank(message = "Il nome dell'elemento è obbligatorio")
    private String nome;

    private String descrizione;

    @NotBlank(message = "La tipologia dell'elemento è obbligatoria")
    private String tipologia;

    public ElementoCatalogoRequestDTO() {
        // costruttore vuoto obbligatorio
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

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }
    
}