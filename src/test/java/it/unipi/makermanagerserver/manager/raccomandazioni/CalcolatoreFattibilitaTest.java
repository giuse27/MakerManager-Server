package it.unipi.makermanagerserver.manager.raccomandazioni;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
     * TEST 1 con BOM1 e DISP1
     * 
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

    /**
     * TEST 2 CON BOM1 e DISP2
     * 
     * In questo test andremo a verificare l'esito di un progetto con un elemento
     * posseduto di cui non si ha sufficiente quantità
     */
    @Test
    void testDettaglioElementiMancantiConQuantitaInsufficiente() {

        EsitoFattibilita esito = calcolatore.calcola(BOM1, DISP2);

        assertFalse(esito.realizzabile());
        assertEquals(1, esito.righeMancanti());
        assertEquals(5, esito.righeTotali());
        assertEquals(0.8, esito.indiceFattibilita());
        assertTrue(esito.dettaglioMancanti().size() == 1);
        assertEquals("Pulsanti", esito.dettaglioMancanti().getFirst().nomeElemento());
        assertEquals(1, esito.dettaglioMancanti().getFirst().quantitaMancante());

    }

    /**
     * TEST 3 con BOM1 e DISP3
     * 
     * In questo test andremo a verificare l'esito di un progetto con un elemento
     * richiesto di cui non si possiede nemmeno una quantità
     */
    @Test
    void testDettaglioElementiMancantiConQuantitaNulla() {

        EsitoFattibilita esito = calcolatore.calcola(BOM1, DISP3);

        assertFalse(esito.realizzabile());
        assertEquals(1, esito.righeMancanti());
        assertEquals(5, esito.righeTotali());
        assertEquals(0.8, esito.indiceFattibilita());
        assertTrue(esito.dettaglioMancanti().size() == 1);
        assertEquals("Led", esito.dettaglioMancanti().getFirst().nomeElemento());
        assertEquals(1, esito.dettaglioMancanti().getFirst().quantitaMancante());
        assertEquals(0, esito.dettaglioMancanti().getFirst().quantitaPosseduta());
        
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

    private final Map<Long, Integer> DISP2 = Map.of(
        // disponibilità nei miei inventari
        // Ho in quantità abbondante quasi tutti gli elementi di BOM1
        // Ma di Pulsanti ne ho solo 1 quando ne sono richiesti 2
        1L, 10, // 10 resistori 100k ohm
        2L, 10, // 10 resistori 330 ohm
        3L, 1,  // 10 Pulsanti
        4L, 10, // 10 Led
        5L, 10  // 10 Arduino Uno
    );

    private final Map<Long, Integer> DISP3 = Map.of(
        // disponibilità nei miei inventari
        // Ho in quantità abbondante quasi tutti gli elementi di BOM1
        // Ma non possiedo i Led
        1L, 10, // 10 resistori 100k ohm
        2L, 10, // 10 resistori 330 ohm
        3L, 10, // 10 Pulsanti
        5L, 10  // 10 Arduino Uno
    );

}
