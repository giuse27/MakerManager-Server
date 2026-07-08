package it.unipi.makermanagerserver.manager.raccomandazioni;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import it.unipi.makermanagerserver.model.catalog.ElementoCatalogo;
import it.unipi.makermanagerserver.model.project.RigaBOM;

/**
 * CalcolatoreFattibilitaTest effettua degli unit test per verificare il calcolo
 * della fattibilità di un progetto, creando delle bom e valutando l'esito
 */
public class CalcolatoreFattibilitaTest {

    private final CalcolatoreFattibilita calcolatore = new CalcolatoreFattibilita();

    // TEST

    /**
     * In questo test passiamo BOM1 e DISP1 e ci aspettiamo che il progetto sia
     * interamente realizzabile
     */
    @Test
    void testProgettoInteramenteRealizzabile() {

        EsitoFattibilita esito = calcolatore.calcola(BOM1, DISP1);

        assertTrue(esito.realizzabile());
        assertEquals(0, esito.righeMancanti());
        assertEquals(5, esito.righeTotali());
        assertEquals(1.0, esito.indiceFattibilita());
        assertTrue(esito.dettaglioMancanti().isEmpty());

    }

    // UTILITY

    /**
     * utility per creare elementi in catalogo sovrascrivendo l'id del db
     */
    private ElementoCatalogo creaElemento(Long id, String nome) {
        ElementoCatalogo elemento = new ElementoCatalogo();
        elemento.setId(id);
        elemento.setNome(nome);
        return elemento;
    }

    // #########################################################################
    // DATI PER I TEST
    // #########################################################################
    
    private final List<RigaBOM> BOM1 = List.of(
        new RigaBOM(
            creaElemento(1L, "Resistore 100k ohm"), 
            2
        ),
        new RigaBOM(
            creaElemento(2L, "Resistore 330 ohm"), 
            1
        ),
        new RigaBOM(
            creaElemento(3L, "Pulsanti"), 
            2
        ),
        new RigaBOM(
            creaElemento(4L, "Led"), 
            1
        ),
        new RigaBOM(
            creaElemento(5L, "Arduino Uno"), 
            1
        )
    );

    private final Map<Long, Integer> DISP1 = Map.of(
        // disponibilità nei miei inventari
        // Ho in quantità abbondante tutti gli elementi di BOM1
        1L, 10, // 10 resistori 100k ohm
        2L, 10, // 10 resistori 330 ohm
        3L, 10, // 10 Pulsanti
        4L, 10, // 10 Led
        5L, 10  // 10 Arduino Uno
    );

}
