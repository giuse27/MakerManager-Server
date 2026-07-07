package it.unipi.makermanagerserver.mapper;

import org.springframework.stereotype.Component;

import it.unipi.makermanagerserver.dto.inventario.ArticoloInventarioRequestDTO;
import it.unipi.makermanagerserver.dto.inventario.ArticoloInventarioResponseDTO;
import it.unipi.makermanagerserver.exception.RisorsaNonTrovataException;
import it.unipi.makermanagerserver.factory.ArticoloInventarioFactory;
import it.unipi.makermanagerserver.model.catalog.ElementoCatalogo;
import it.unipi.makermanagerserver.model.inventory.ArticoloInventario;
import it.unipi.makermanagerserver.model.inventory.Inventario;
import it.unipi.makermanagerserver.repository.ElementoCatalogoRepository;
import it.unipi.makermanagerserver.repository.InventarioRepository;

/**
 * Mapper dedicato alla conversione tra l'entity Inventario e i suoi
 * DTO di richiesta/risposta 
 * 
 * @Component la registra come bean Spring, cosi' puo' essere iniettata via
 * costruttore in qualunque service ne abbia bisogno
 */
@Component
public class ArticoloInventarioMapper {

    private final ElementoCatalogoRepository elementoCatalogoRepository;
    private final InventarioRepository inventarioRepository;

    public ArticoloInventarioMapper(
        ElementoCatalogoRepository elementoCatalogoRepository,
        InventarioRepository inventarioRepository
    ) {
        
        this.elementoCatalogoRepository = elementoCatalogoRepository;
        this.inventarioRepository = inventarioRepository;

    }

    /**
     * @throws RisorsaNonTrovataException se l'id dell'elemento di catalogo o
     *         dell'inventario indicati nel DTO non corrispondono a nessuna
     *         entita' esistente
     */
    public ArticoloInventario toArticoloInventario(ArticoloInventarioRequestDTO dto) {

        // 1. Cerchiamo l'ElementoCatalogo nel DB usando l'id fornito nel DTO
        ElementoCatalogo elemento = elementoCatalogoRepository.findById(dto.getIdElementoCatalogo())
                .orElseThrow(() -> new RisorsaNonTrovataException(
                        "Impossibile aggiungere l'articolo: Elemento Catalogo non trovato con ID " + dto.getIdElementoCatalogo()
                ));

        // 2. Cerchiamo l'Inventario nel DB usando l'id fornito nel DTO
        Inventario inventario = inventarioRepository.findById(dto.getIdInventario())
                .orElseThrow(() -> new RisorsaNonTrovataException(
                        "Impossibile aggiungere l'articolo: Inventario non trovato con ID " + dto.getIdInventario()
                ));

        // 3. Estraiamo il "tipo" (Stringa) dall'ElementoCatalogo. 
        // Supponendo che ElementoCatalogo abbia un getter getTipologia() che restituisce l'Enum.
        String tipo = elemento.getTipologia().name();

        // 4. Deleghiamo la creazione fisica dell'oggetto alla nostra Factory
        return ArticoloInventarioFactory.creaArticoloInventario(
                tipo, 
                elemento, 
                inventario, 
                dto.getQuantita()
        );
        
    }

    public ArticoloInventarioResponseDTO toResponseDTO(ArticoloInventario articolo) {

        return new ArticoloInventarioResponseDTO(
            articolo.getId(),
            articolo.getElementoCatalogo().getNome(),
            articolo.getInventario().getNome(),
            articolo.getQuantita()
        );

    }

}