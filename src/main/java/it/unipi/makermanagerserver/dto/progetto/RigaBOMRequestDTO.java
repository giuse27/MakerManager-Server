package it.unipi.makermanagerserver.dto.progetto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO di RICHIESTA: dati che il client invia per aggiungere una nuova
 * riga alla B.O.M. di un progetto (POST /api/progetti/{idProgetto}/bom).
 */
public class RigaBOMRequestDTO {

    @NotNull(message = "L'id di riferimento all'elemento in catalogo è obbligatorio")
    private Long idElementoCatalogo;

    @Min(value = 1, message = "La quantità minima è 1")
    private int quantita;

    public RigaBOMRequestDTO() {
    }

    public Long getIdElementoCatalogo() {
        return idElementoCatalogo;
    }

    public void setIdElementoCatalogo(Long idElementoCatalogo) {
        this.idElementoCatalogo = idElementoCatalogo;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

}