package it.unipi.makermanagerserver.dto.progetto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO di RICHIESTA: dati che il client invia per creare un nuovo
 * ProgettoMaker (POST /api/progetti).
 *
 * "tipo" resta una String (non l'enum TipologiaProgetto) per lo stesso
 * motivo visto in ElementoCatalogoRequestDTO: la conversione avviene nel
 * mapper, cosi' un valore non valido produce un errore controllato invece
 * di far fallire Jackson prima ancora di entrare nel controller.
 *
 * La BOM non e' presente qui: verra' gestita da endpoint dedicati
 */
public class ProgettoRequestDTO {

    @NotBlank(message = "Il tipo di progetto è obbligatorio")
    private String tipo;

    @NotBlank(message = "Il nome del progetto è obbligatorio")
    private String nome;

    private String descrizione;

    public ProgettoRequestDTO() {
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

}