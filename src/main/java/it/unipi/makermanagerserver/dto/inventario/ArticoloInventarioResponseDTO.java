package it.unipi.makermanagerserver.dto.inventario;

public class ArticoloInventarioResponseDTO {
    
    private Long id;
    private String elementoCatalogo;
    private String inventario;
    private int quantita;

    public ArticoloInventarioResponseDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getElementoCatalogo() {
        return elementoCatalogo;
    }

    public void setElementoCatalogo(String elementoCatalogo) {
        this.elementoCatalogo = elementoCatalogo;
    }

    public String getInventario() {
        return inventario;
    }

    public void setInventario(String inventario) {
        this.inventario = inventario;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

}
