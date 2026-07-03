package it.unipi.makermanagerserver.dto.inizializza;

/**
 * DTO "di caricamento" per una voce dell'array "articoliInventario" nel
 * JSON di inizializzazione. Vedi ElementoCatalogoInitDTO per la spiegazione
 * generale del ruolo di questi DTO.
 *
 * NOTA BENE: elementoCatalogo e inventario qui sono semplici NOMI (String),
 * non le entity vere e proprie. Nel JSON non possiamo conoscere gli ID
 * generati dal database (GenerationType.IDENTITY viene assegnato solo al
 * salvataggio), quindi il service dovra' risolvere questi nomi cercando le
 * entity gia' salvate in precedenza (ElementoCatalogo viene sempre caricato
 * prima, essendo la base di tutto il dominio).
 */
public class ArticoloInventarioInitDTO {
 
    private String tipo;
    private String elementoCatalogo;
    private String inventario;
    private int quantita;

    public ArticoloInventarioInitDTO() {

    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
