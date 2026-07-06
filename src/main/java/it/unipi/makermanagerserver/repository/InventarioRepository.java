package it.unipi.makermanagerserver.repository;

import it.unipi.makermanagerserver.model.inventory.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    // Cerca un inventario per nome, utile all'InizializzazioneService
    // per risolvere i riferimenti testuali presenti nel JSON di caricamento
    Optional<Inventario> findByNome(String nome);

    // Cerca gli inventari appartenenti a un utente, navigando la relazione
    // "utente" (ManyToOne) e poi il suo id.
    List<Inventario> findByUtenteId(Long idUtente);

}