package it.unipi.makermanagerserver.manager.raccomandazioni;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import it.unipi.makermanagerserver.model.catalog.ElementoCatalogo;
import it.unipi.makermanagerserver.model.project.RigaBOM;

/**
 * CalcolatoreFattibilita si occupa nell'effettivo di indicizzare i progetti 
 * per poi consigliarli agli utenti
 * 
 * Il calcolatore non dipende da spring e riceve solo i parametri restituendo
 * il record immutabile EsitoFattibilita. NOTA BENE: la gestione delle attese
 * attive la gestisce spring, in quanto ogni richiesta http gira su un thread, 
 * e il calcolatore viene chiamato da uno di questi thread
 * 
 * in futuro potrei implementare un sistema di cache e in quel caso ci dovrà
 * essere un thread che gira in background indipendente dalle chiamate http
 * 
 * La classe è comunque annotata con @Component per dependency injection
 */
@Component
public class CalcolatoreFattibilita {

    /**
     * Metodo di calcolo effettivo per la fabbibilità del progetto
     * 
     * La BOM viene prima ripulita dai duplicati di cui vengono sommate le
     * quantita: se lo stesso ElementoCatalogo compare su piu' righe,
     * le quantità richieste vengono sommate. In questo modo "elemento
     * mancante" significa davvero "elemento distinto non coperto".
     *
     * @param righeBom righe della bom del progetto
     * @param disponibilita mappa idElementoCatalogo -> quantita' posseduta,
     *                      gia' aggregata su tutti gli inventari dell'utente
     * @return l'esito immutabile del calcolo
     */
    public EsitoFattibilita calcola(
        List<RigaBOM> righeBom,
        Map<Long, Integer> disponibilita
    ) {

        Map<Long, FabbisognoElemento> bomElementiUnici = rimuoviDuplicati(righeBom);

        // costruisco la lista per gli elementi mancanti contenuta in Esito
        List<ElementoMancante> mancanti = new ArrayList<>();

        for (FabbisognoElemento requisito : bomElementiUnici.values()) {

            // prelevo la quantità posseduta dalle mie disponibilita
            Long id = requisito.id;
            int posseduto = disponibilita.getOrDefault(id, 0);

            // verifico se il requisito è rispettato
            if (posseduto < requisito.quantitaRichiesta()) {
                mancanti.add(
                    new ElementoMancante(
                        requisito.nome(),
                        requisito.quantitaRichiesta(),
                        posseduto,
                        requisito.quantitaRichiesta() - posseduto
                    )  
                );
            }
            
        }

        return new EsitoFattibilita(bomElementiUnici.size(), mancanti);

    }

    // UTILITY

    /**
     * Una bom potrebbe avere duplicati tra i suoi elementi (magari è divisa per
     * sezioni e quindi nella prima sezione potrebbero esserci 6 viti, mentre
     * nella seconda sezione essercene 10), a me però interessa la quantità
     * totale di ciascun elemento
     * 
     * Questa utility serve per creare una mappa che a partire dalla bom del 
     * progetto somma le quantità di ciascun elemento e le associa al loro id
     * 
     * @param righeBom righe della bom del progetto
     * @return restituisce una mappa che associa l'id di un elemento alla sua
     *          quantita totale richiesta
     */
    private Map<Long, FabbisognoElemento> rimuoviDuplicati(List<RigaBOM> righeBom) {
        
        // uso una linked hash map perché ha il vantaggio di mantenere l'ordine
        // di inserimento (ordine della bom)
        Map<Long, FabbisognoElemento> fabbisogni = new LinkedHashMap<>();

        for (RigaBOM rigaBOM : righeBom) {

            ElementoCatalogo elemento = rigaBOM.getArticoloRichiesto();
            Long id = elemento.getId();

            int quantitaTotale = rigaBOM.getQuantitaRichiesta();
            
            // cerco l'elemento per vedere se già esiste nella mappa (duplicato)
            // altrimenti restituirà null (prima occorrenza)
            FabbisognoElemento fabbisognoEsistente = fabbisogni.get(id);

            // sommo le quantità richieste se l'elemento è duplicato
            if (fabbisognoEsistente != null) {
                quantitaTotale += fabbisognoEsistente.quantitaRichiesta();
            }

            // aggiungo o aggiorno la mappa
            fabbisogni.put(
                id, 
                new FabbisognoElemento(
                    id, 
                    elemento.getNome(), 
                    quantitaTotale
                )
            );
            
        }

        return fabbisogni;

    }

    private record FabbisognoElemento(Long id, String nome, int quantitaRichiesta) {
    }

}
