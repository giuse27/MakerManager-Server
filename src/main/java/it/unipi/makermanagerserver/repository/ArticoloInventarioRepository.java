package it.unipi.makermanagerserver.repository;

import it.unipi.makermanagerserver.model.inventory.ArticoloInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArticoloInventarioRepository extends JpaRepository<ArticoloInventario, Long> {
    
    // Cerca in base alla quantità
    List<ArticoloInventario> findByQuantitaLessThanEqual(int soglia);
    
    // Cerca un oggetto nell'inventario partendo dal suo descriptor teorico del catalogo
    List<ArticoloInventario> findByElementoCatalogoId(Long elementoCatalogoId);
    
}