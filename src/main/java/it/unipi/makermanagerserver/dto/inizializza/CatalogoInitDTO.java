package it.unipi.makermanagerserver.dto.inizializza;

import java.util.List;

/**
 * DTO radice che rispecchia l'intera struttura del file
 * catalogo-iniziale.json (le 4 sezioni: catalogo, inventari,
 * articoliInventario, progetti).
 *
 * Jackson (gia' incluso in spring-boot-starter-webmvc) mappa
 * automaticamente le proprieta' del JSON sui campi con lo stesso nome,
 * senza bisogno di configurazione aggiuntiva: e' per questo che i nomi
 * dei campi qui sotto devono corrispondere esattamente alle chiavi
 * di primo livello nel JSON.
 */
public class CatalogoInitDTO {

    private List<ElementoCatalogoInitDTO> catalogo;
    private List<InventarioInitDTO> inventari;
    private List<ArticoloInventarioInitDTO> articoliInventario;
    private List<ProgettoInitDTO> progetti;

    public CatalogoInitDTO() {

    }

    public List<ElementoCatalogoInitDTO> getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(List<ElementoCatalogoInitDTO> catalogo) {
        this.catalogo = catalogo;
    }

    public List<InventarioInitDTO> getInventari() {
        return inventari;
    }

    public void setInventari(List<InventarioInitDTO> inventari) {
        this.inventari = inventari;
    }

    public List<ArticoloInventarioInitDTO> getArticoliInventario() {
        return articoliInventario;
    }

    public void setArticoliInventario(List<ArticoloInventarioInitDTO> articoliInventario) {
        this.articoliInventario = articoliInventario;
    }

    public List<ProgettoInitDTO> getProgetti() {
        return progetti;
    }

    public void setProgetti(List<ProgettoInitDTO> progetti) {
        this.progetti = progetti;
    }
    
}
