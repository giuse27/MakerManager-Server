package it.unipi.makermanagerserver.mapper;

import org.springframework.stereotype.Component;

import it.unipi.makermanagerserver.dto.catalogo.ElementoCatalogoRequestDTO;
import it.unipi.makermanagerserver.dto.catalogo.ElementoCatalogoResponseDTO;
import it.unipi.makermanagerserver.enums.TipologiaElemento;
import it.unipi.makermanagerserver.model.catalog.ElementoCatalogo;

/**
 * Mapper dedicato alla conversione tra l'entity ElementoCatalogo e i suoi
 * DTO di richiesta/risposta (SRP: questa classe non contiene ne' logica di
 * business ne' accesso al database, solo trasformazione di formato).
 *
 * @Component la registra come bean Spring, cosi' puo' essere iniettata via
 * costruttore in qualunque service ne abbia bisogno (qui CatalogoService),
 * coerentemente con la convenzione di iniezione gia' usata nel progetto.
 */
@Component
public class ElementoCatalogoMapper {
    
    /**
     * Converte un RequestDTO in una nuova entity, pronta per essere salvata.
     * Non imposta l'id: verra' generato dal database al momento del save().
     */
    public ElementoCatalogo toElemento(ElementoCatalogoRequestDTO dto) {

        return new ElementoCatalogo(
            dto.getNome(),
            dto.getDescrizione(),
            TipologiaElemento.valueOf(dto.getTipologia())
        );

    }

    /**
     * Converte un'entity gia' creata in un ResponseDTO da restituire al client.
     */
    public ElementoCatalogoResponseDTO toResponseDTO(ElementoCatalogo elemento) {

        return new ElementoCatalogoResponseDTO(
            elemento.getId(),
            elemento.getNome(),
            elemento.getDescrizione(),
            elemento.getTipologia().name()
        );

    }

}
