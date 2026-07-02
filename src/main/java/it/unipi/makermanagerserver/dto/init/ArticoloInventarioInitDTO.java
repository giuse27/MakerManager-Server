package it.unipi.makermanagerserver.dto.init;

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
    
}
