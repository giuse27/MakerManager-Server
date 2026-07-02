package it.unipi.makermanagerserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.unipi.makermanagerserver.model.inventory.ArticoloInventario;

@Repository
public interface ArticoloInventarioRepository extends JpaRepository<ArticoloInventario, Long> {
    
    // Trova gli articoli che stanno per finire nel cassetto (Quantità minore o uguale a una soglia)
    List<ArticoloInventario> findByQuantitaInPossessoLessThanEqual(int soglia);
    
    // Cerca un oggetto nell'inventario partendo dal suo descriptor teorico del catalogo
    List<ArticoloInventario> findByElementoCatalogoId(Long elementoCatalogoId);
    
}
