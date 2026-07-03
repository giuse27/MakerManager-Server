package it.unipi.makermanagerserver.service.endpoint;

import java.util.List;

import org.springframework.stereotype.Service;

import it.unipi.makermanagerserver.dto.inventario.ArticoloInventarioRequestDTO;
import it.unipi.makermanagerserver.dto.inventario.ArticoloInventarioResponseDTO;
import it.unipi.makermanagerserver.dto.inventario.InventarioRequestDTO;
import it.unipi.makermanagerserver.dto.inventario.InventarioResponseDTO;
import it.unipi.makermanagerserver.exception.RisorsaNonTrovataException;
import it.unipi.makermanagerserver.mapper.ArticoloInventarioMapper;
import it.unipi.makermanagerserver.mapper.InventarioMapper;
import it.unipi.makermanagerserver.model.inventory.ArticoloInventario;
import it.unipi.makermanagerserver.model.inventory.Inventario;
import it.unipi.makermanagerserver.repository.ArticoloInventarioRepository;
import it.unipi.makermanagerserver.repository.ElementoCatalogoRepository;
import it.unipi.makermanagerserver.repository.InventarioRepository;

/**
 * Questa classe fornisce metodi per l'endpoint /api/inventario/*
 * 
 * Il controller usa questa classe come intermediaria con il repository o mapper
 */
@Service
public class InventarioService {

    private final InventarioRepository inventarioRepo;
    private final ArticoloInventarioRepository articoloRepo;
    private final InventarioMapper inventarioMapper;
    private final ArticoloInventarioMapper articoloMapper;
    private final ElementoCatalogoRepository elementoRepo;

    public InventarioService(
        InventarioRepository inventarioRepo,
        ArticoloInventarioRepository articoloRepo,
        InventarioMapper inventarioMapper,
        ArticoloInventarioMapper articoloMapper,
        ElementoCatalogoRepository elementoRepo
    ) {

        this.inventarioRepo = inventarioRepo;
        this.articoloRepo = articoloRepo;
        this.inventarioMapper = inventarioMapper;
        this.articoloMapper = articoloMapper;
        this.elementoRepo = elementoRepo;

    }

    /**
     * Restituisce tutti gli inventari presenti nel database, convertiti in DTO di risposta.
     */
    public List<InventarioResponseDTO> trovaTuttiGliInventari() {

        return inventarioRepo.findAll()
                    .stream()
                    .map(inventarioMapper::toResponseDTO)
                    .toList();

    }

    /**
     * Restituisce il contenuto (lista di articoli) dell'inventario indicato.
     * 
     * @param idInventario id dell'inventario da cercare
     * @throws RisorsaNonTrovataException se l'inventario non esiste
     */
    public List<ArticoloInventarioResponseDTO> trovaInventario(Long idInventario) {

        Inventario inventario = inventarioRepo.findById(idInventario)
                .orElseThrow(() -> new RisorsaNonTrovataException(
                        "Inventario con id " + idInventario + " non trovato"
                ));

        return inventario.getArticoli()
                    .stream()
                    .map(articoloMapper::tResponseDTO)
                    .toList();

    }

    /**
     * Restituisce l'elenco degli inventari appartenenti a un utente.
     * 
     * @param idUtente id dell'utente di cui cercare gli inventari
     */
    public List<InventarioResponseDTO> trovaInventariUtente(Long idUtente) {

        return inventarioRepo.findByIdUtente(idUtente)
                    .stream()
                    .map(inventarioMapper::toResponseDTO)
                    .toList();

    }

    /**
     * Crea un nuovo inventario a partire dal DTO di richiesta.
     * 
     * @param dto dati del nuovo inventario (nome, idUtente)
     * @return il DTO di risposta, comprensivo dell'id generato dal database
     */
    public InventarioResponseDTO creaInventario(InventarioRequestDTO dto) {

        Inventario inventario = inventarioMapper.toInventario(dto);
        Inventario inventarioDB = inventarioRepo.save(inventario);

        return inventarioMapper.toResponseDTO(inventarioDB);

    }

    /**
     * Elimina l'inventario indicato.
     * 
     * Nota: Inventario.articoli e' mappato con cascade = CascadeType.ALL e
     * orphanRemoval = true (vedi Inventario.java), quindi eliminando l'inventario
     * Hibernate elimina automaticamente anche tutti i suoi ArticoloInventario:
     * non serve alcuna cancellazione manuale qui.
     * 
     * @param idInventario id dell'inventario da eliminare
     * @throws RisorsaNonTrovataException se l'inventario non esiste
     */
    public void eliminaInventario(Long idInventario) {

        if (!inventarioRepo.existsById(idInventario)) {
            throw new RisorsaNonTrovataException(
                "Inventario con id " + idInventario + " non trovato"
            );
        }
        inventarioRepo.deleteById(idInventario);

    }

    /**
     * Crea un nuovo ArticoloInventario. L'inventario e l'elemento di catalogo
     * di destinazione sono indicati dagli id gia' presenti nel DTO
     * (ArticoloInventarioMapper.toArticoloInventario() li risolve internamente).
     * 
     * @param dto dati del nuovo articolo (idElementoCatalogo, idInventario, quantita)
     * @return il DTO di risposta, comprensivo dell'id generato dal database
     */
    public ArticoloInventarioResponseDTO creaArticoloInventario(ArticoloInventarioRequestDTO dto) {

        ArticoloInventario articolo = articoloMapper.toArticoloInventario(dto);
        ArticoloInventario articoloDB = articoloRepo.save(articolo);

        return articoloMapper.tResponseDTO(articoloDB);

    }

    /**
     * Elimina l'articolo indicato dall'inventario.
     * 
     * A differenza della creazione, qui non riceviamo l'id dell'inventario nel
     * body ma solo l'id dell'articolo: il metodo verifica comunque che
     * l'articolo esista prima di cancellarlo.
     * 
     * @param idArticolo id dell'articolo da eliminare
     * @throws RisorsaNonTrovataException se l'articolo non esiste
     */
    public void eliminaArticoloDaInventario(Long idArticolo) {

        if (!articoloRepo.existsById(idArticolo)) {
            throw new RisorsaNonTrovataException(
                "Articolo con id " + idArticolo + " non trovato"
            );
        }
        articoloRepo.deleteById(idArticolo);

    }

}