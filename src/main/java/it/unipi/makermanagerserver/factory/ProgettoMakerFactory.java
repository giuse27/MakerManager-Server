package it.unipi.makermanagerserver.factory;

import it.unipi.makermanagerserver.model.project.ProgettoMaker;
import it.unipi.makermanagerserver.model.project.specific.ProgettoElettronica;
import it.unipi.makermanagerserver.model.project.specific.ProgettoRobotica;
import it.unipi.makermanagerserver.model.project.specific.ProgettoSoftware;
import it.unipi.makermanagerserver.model.project.specific.ProgettoStampa3D;

/**
 * TODO
 * NOTA PER IL FUTURO devi creare un enum TipologiaProgetto e poi aggiornare qui
 */
public class ProgettoMakerFactory {
    
    // Costruttore privato: essendo una classe di utilità non voglio che qualcuno
    // ne possa creare un'istanza, e invece userò metodi statici
    private ProgettoMakerFactory() {
        throw new UnsupportedOperationException(
            "Questa è una classe factory di utilità e non può essere istanziata."
        );
    }

    public static ProgettoMaker creaProgetto(String tipo) {
        
        return switch (tipo) {

            case "STAMPA_3D" -> new ProgettoStampa3D();
            case "ELETTRONICA" -> new ProgettoElettronica();
            case "ROBOTICA" -> new ProgettoRobotica();
            case "SOFTWARE" -> new ProgettoSoftware();

            default -> throw new IllegalArgumentException(
                "Tipologia di articolo non ancora supportata: " + tipo 
            );

        };

    }

}
