package it.unipi.makermanagerserver.model.project;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * --- W.I.P. ---
 * 
 * Implementazione temporanea
 */
@Embeddable
public class BOM {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "progetto_id") // Aggiunge questa colonna (chiave esterna) nella tabella righe_bom
    private List<RigaBOM> righeFabbisogno = new ArrayList<>();

    // Costruttori

    public BOM() {
    }

    // Metodi Helper

    // Metodi di utilità per aggiungere o rimuovere pezzi dalla distinta base
    public void aggiungiRiga(RigaBOM riga) {
        this.righeFabbisogno.add(riga);
    }

    public void rimuoviRiga(RigaBOM riga) {
        this.righeFabbisogno.remove(riga);
    }

    // Getter e Setter

    public List<RigaBOM> getRigheFabbisogno() {
        return righeFabbisogno;
    }

    public void setRigheFabbisogno(List<RigaBOM> righeFabbisogno) {
        this.righeFabbisogno = righeFabbisogno;
    }
    
}