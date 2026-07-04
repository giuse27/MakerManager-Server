package it.unipi.makermanagerserver.dto.inventario;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ArticoloInventarioRequestDTO {

    /*
        A differenza dei DTO di inizializza qui possiamo passare i riferimenti
        all'elemento in catalogo o all'inventario direttamente tramite gli id
        perché c'è il prerequisito che l'inventario o l'elemento in catalogo
        devono essere stati già creati
     */
    
    @NotNull(message = "L'id di riferimento all'elemento in catalogo è obbligatorio")
    private Long idElementoCatalogo;

    @NotNull(message = "L'id di riferimento all'inventario è obbligatorio")
    private Long idInventario;

    @Min(value = 1, message = "La quantità minima è 1")
    private int quantita;

    public ArticoloInventarioRequestDTO() {

    }

    public Long getIdElementoCatalogo() {
        return idElementoCatalogo;
    }

    public void setIdElementoCatalogo(Long idElementoCatalogo) {
        this.idElementoCatalogo = idElementoCatalogo;
    }
    
    public Long getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(Long idInventario) {
        this.idInventario = idInventario;
    }
    
    public int getQuantita() {
        return quantita;
    }
    
    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

}
