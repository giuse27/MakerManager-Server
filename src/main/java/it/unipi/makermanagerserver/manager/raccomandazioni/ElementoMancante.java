package it.unipi.makermanagerserver.manager.raccomandazioni;

/**
 * Descrizione di un ElementoCatalogo di cui l'utente non possiede abbastanza 
 * pezzi per realizzare un progetto.
 *
 * Viene prodotto da CalcolatoreFattibilita e non va considerato ne come modello
 * ne come dto. la conversione verso il client e' compito di RaccomandazioneMapper.
 *
 * @param nomeElemento nome dell'ElementoCatalogo mancante
 * @param quantitaRichiesta quantita' totale richiesta dalla BOM per quell'elemento
 * @param quantitaPosseduta quantita' effettivamente posseduta in inventario
 * @param quantitaMancante pezzi che mancano (quantitaRichiesta - quantitaPosseduta)
 */
public record ElementoMancante(
    String nomeElemento,
    int quantitaRichiesta,
    int quantitaPosseduta,
    int quantitaMancante
) {
}