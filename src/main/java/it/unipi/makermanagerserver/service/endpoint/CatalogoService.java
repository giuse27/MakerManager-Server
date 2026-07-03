package it.unipi.makermanagerserver.service.endpoint;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import it.unipi.makermanagerserver.dto.catalogo.ElementoCatalogoRequestDTO;
import it.unipi.makermanagerserver.dto.catalogo.ElementoCatalogoResponseDTO;
import it.unipi.makermanagerserver.mapper.ElementoCatalogoMapper;
import it.unipi.makermanagerserver.model.catalog.ElementoCatalogo;
import it.unipi.makermanagerserver.repository.ElementoCatalogoRepository;

/**
 * Questa classe fornisce metodi per l'endpoint /api/catalogo
 * 
 * Il controller usa questa classe come intermediaria con il repository o mapper
 */
@Service
public class CatalogoService {

    private final ElementoCatalogoRepository catalogoRepo;
    private final ElementoCatalogoMapper catalogoMapper;

    public CatalogoService(
        ElementoCatalogoRepository catalogoRepo, 
        ElementoCatalogoMapper catalogoMapper
    ) {

        this.catalogoRepo = catalogoRepo;
        this.catalogoMapper = catalogoMapper;
    
    }
    
    /**
     * Cerca tutti gli elementi presenti in catalogo
     * 
     * @return Restituisce la lista degli elementi in catalogo in dto di risposta
     */ 
    public List<ElementoCatalogoResponseDTO> trovaTutti() {

        // restituisce tutti gli elementi catalogo convertiti in dto di risposta
        return catalogoRepo.findAll()
                    /*
                        Apre uno stream, ovvero permette di eseguire delle
                        operazioni su ogni elemento trovato da findAll() senza
                        scrivere cicli for o ulteriore codice
                     */
                    .stream()
                    /*
                        Questa operazione viene compiuta su ciascun elemento e
                        prende ciascun ElementoCatalogo trovato da findAll() e
                        lo passo automaticamente a toResponseDTO (::toResponseDTO())
                        così facendo la map trasforma gli elementi in dto di risposta
                     */
                    .map(catalogoMapper::toResponseDTO)
                    /*
                        Solo infine ricostruiamo il tutto sotto dorma di lista
                        return List<ElementoCatalogoResponseDTO>
                     */
                    .toList();

    }

    /**
     * Crea un nuovo ElementoCatalogo
     * 
     * @param dto Prende in input un dto di richiesta
     * @return Restituisce un dto di risposta
     */
    public ElementoCatalogoResponseDTO crea(ElementoCatalogoRequestDTO dto) {

        // creo un nuovo elemento privo di id
        ElementoCatalogo elemento = catalogoMapper.toElemento(dto);
        // lo salvo per ottenere il suo id
        ElementoCatalogo elementoDB = catalogoRepo.save(elemento);

        // restituisco il dto di risposta aggiornato con l'id
        return catalogoMapper.toResponseDTO(elementoDB);

    }

    /**
     * Elimina un elemento dal catalogo
     * 
     * Nota per il futuro: ElementoCatalogo puo' essere referenziato da
     * ArticoloInventario e RigaBOM (@ManyToOne senza cascade, come visto
     * per /inizializza). Se in futuro un elemento risultasse ancora
     * referenziato al momento della cancellazione, il database rifiutera'
     * l'operazione per vincolo di integrita' referenziale: per ora lasciamo
     * che l'eccezione risalga al controller, che la trasformera' in una
     * risposta HTTP di errore.
     * 
     * @param id id dell'elemento da eliminare
     */
    public void elimina(Long id) {

        if (!catalogoRepo.existsById(id)) {
            throw new NoSuchElementException(
                "ElementoCatalogo con id " + id + " non trovato"
            );
        }
        catalogoRepo.deleteById(id);

    }

}
