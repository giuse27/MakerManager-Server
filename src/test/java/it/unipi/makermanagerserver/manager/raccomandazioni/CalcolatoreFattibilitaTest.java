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

    /**
     * TEST 4 con BOM2 e DISP4
     * 
     * Ho alcuni elementi duplicati nella bom e voglio 
     * vedere se la quantità totale è corretta, per farlo testo su un progetto 
     * di cui mi mancano alcuni pezzi
     */
    @Test
    void testAggregazioneElementiRipetuti() {
        
        EsitoFattibilita esito = calcolatore.calcola(BOM2, DISP4);

        assertFalse(esito.realizzabile());
        assertEquals(2, esito.righeMancanti());
        assertEquals(5, esito.righeTotali());
        assertEquals(0.6, esito.indiceFattibilita());
        // testo implicitamente anche l'ordine di comparizione
        assertEquals(30, esito.dettaglioMancanti().getFirst().quantitaRichiesta());
        assertEquals(27, esito.dettaglioMancanti().getFirst().quantitaPosseduta());
        assertEquals(3, esito.dettaglioMancanti().getFirst().quantitaMancante());
        assertEquals(30, esito.dettaglioMancanti().getLast().quantitaRichiesta());
        assertEquals(13, esito.dettaglioMancanti().getLast().quantitaPosseduta());
        assertEquals(17, esito.dettaglioMancanti().getLast().quantitaMancante());
        assertEquals(20, esito.pezziMancantiTotali());

    }

    /**
     * TEST 5 con BOM2 e inventario vuoto
     * 
     * Mi aspetto che sia tutto mancante, il test valuta anche la correttezza
     * di pezziMancantiTotali()
     */
    @Test
    void testInventarioVuotoRendeTuttoMancante() {
 
        EsitoFattibilita esito = calcolatore.calcola(BOM2, Map.of());
 
        assertFalse(esito.realizzabile());
        assertEquals(5, esito.righeMancanti());
        assertEquals(0.0, esito.indiceFattibilita());
        assertEquals(63, esito.pezziMancantiTotali());

    }

    /**
     * TEST 6 con BOM vuota e inventario vuoto
     * 
     * Mi aspetto che sia indice = 1.0, effettuo questo test per vedere se ci
     * sono punti sensibili alla divisione per 0. Questo test nella pratica non
     * compare mai in quanto i progetti con bom vuota vengono scartati
     */
    @Test
    void testInventarioEBomVuota() {
 
        EsitoFattibilita esito = calcolatore.calcola(List.of(), Map.of());
 
        assertTrue(esito.realizzabile());
        assertEquals(0, esito.righeMancanti());
        assertEquals(1.0, esito.indiceFattibilita());
        assertEquals(0, esito.pezziMancantiTotali());

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

    private final ElementoCatalogo elementoRipetuto1 = creaElemento(1L, "Resistore 100k ohm");
    
    private final List<RigaBOM> BOM2 = List.of(
        new RigaBOM(
            elementoRipetuto1,
            10
        ),
        new RigaBOM(
            creaElemento(2L, "Resistore 330 ohm"), 
            1
        ),
        new RigaBOM(
            creaElemento(3L, "Pulsanti"), 
            30
        ),
        new RigaBOM(
            creaElemento(4L, "Led"), 
            1
        ),
        new RigaBOM(
            creaElemento(5L, "Arduino Uno"), 
            1
        ),
        new RigaBOM(
            elementoRipetuto1,
            20
        )
    );

    private final Map<Long, Integer> DISP4 = Map.of(
        // disponibilità nei miei inventari
        1L, 27, // 10 resistori 100k ohm
        2L, 10, // 10 resistori 330 ohm
        3L, 13, // 10 Pulsanti
        4L, 10, // 10 Led
        5L, 10  // 10 Arduino Uno
    );

}
