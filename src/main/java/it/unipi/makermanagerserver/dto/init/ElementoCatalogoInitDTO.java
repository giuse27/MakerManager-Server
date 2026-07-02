package it.unipi.makermanagerserver.dto.init;

/**
 * DTO "di caricamento": rispecchia la forma di una voce dell'array
 * "catalogo" nel file catalogo-iniziale.json.
 *
 * A differenza di un DTO di risposta API, questo non viene mai esposto
 * al client: serve solo come struttura intermedia per deserializzare il
 * JSON con Jackson, prima che il service lo converta in entity ElementoCatalogo.
 *
 * Nota: il campo "tipologia" resta una String (non l'enum TipologiaElemento)
 * perche' la conversione avviene nel service: se il JSON contiene
 * un valore non valido vogliamo poterlo intercettare noi con un errore chiaro
 */
public class ElementoCatalogoInitDTO {
    
}
