package it.unipi.makermanagerserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.unipi.makermanagerserver.enums.TipologiaElemento;
import it.unipi.makermanagerserver.model.catalog.ElementoCatalogo;

@Repository
public interface ElementoCatalogoRepository extends JpaRepository<ElementoCatalogo, Long>{
    
    // le query sql vengono fatte in automatico da spring a partire dal parsing
    // testuale del nome del metodo
    // SELECT * FROM elementi_catalogo WHERE nome LIKE %?%
    List<ElementoCatalogo> findByNomeContainingIgnoreCase(String nome);

    // trova componenti per tipologia
    List<ElementoCatalogo> findByTipologia(TipologiaElemento tipologia);

}