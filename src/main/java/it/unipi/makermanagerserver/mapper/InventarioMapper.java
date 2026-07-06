package it.unipi.makermanagerserver.mapper;

import org.springframework.stereotype.Component;

import it.unipi.makermanagerserver.dto.inventario.InventarioRequestDTO;
import it.unipi.makermanagerserver.dto.inventario.InventarioResponseDTO;
import it.unipi.makermanagerserver.model.inventory.Inventario;
import it.unipi.makermanagerserver.model.user.Utente;

/**
 * Mapper dedicato alla conversione tra l'entity Inventario e i suoi
 * DTO di richiesta/risposta 
 * 
 * @Component la registra come bean Spring, cosi' puo' essere iniettata via
 * costruttore in qualunque service ne abbia bisogno
 */
@Component
public class InventarioMapper {

    public Inventario toInventario(InventarioRequestDTO dto, Utente utente) {

        return new Inventario(
            dto.getNome(),
            utente
        );

    }

    public InventarioResponseDTO toResponseDTO(Inventario inventario) {

        return new InventarioResponseDTO(
            inventario.getId(),
            inventario.getNome(),
            inventario.getUtente().getId(),
            inventario.getUtente().getNickname()
        );

    }
    
}
