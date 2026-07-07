package it.unipi.makermanagerserver.service.endpoint;

import java.util.List;

import org.springframework.stereotype.Service;

import it.unipi.makermanagerserver.dto.common.AggiornaQuantitaDTO;
import it.unipi.makermanagerserver.dto.inventario.ArticoloInventarioRequestDTO;
import it.unipi.makermanagerserver.dto.inventario.ArticoloInventarioResponseDTO;
import it.unipi.makermanagerserver.dto.inventario.InventarioRequestDTO;
import it.unipi.makermanagerserver.dto.inventario.InventarioResponseDTO;
import it.unipi.makermanagerserver.exception.RisorsaNonTrovataException;
import it.unipi.makermanagerserver.mapper.ArticoloInventarioMapper;
import it.unipi.makermanagerserver.mapper.InventarioMapper;
import it.unipi.makermanagerserver.model.inventory.ArticoloInventario;
import it.unipi.makermanagerserver.model.inventory.Inventario;
import it.unipi.makermanagerserver.model.user.Utente;
import it.unipi.makermanagerserver.repository.ArticoloInventarioRepository;
import it.unipi.makermanagerserver.repository.ElementoCatalogoRepository;
import it.unipi.makermanagerserver.repository.InventarioRepository;
import it.unipi.makermanagerserver.security.UtenteCorrente;

/**
 * Questa classe fornisce metodi per l'endpoint /api/inventario/*
 *
 * Il controller usa questa classe come intermediaria con il repository o mapper.
 * Un inventario e' un dato personale: oltre al filtro sui ruoli applicato a
 * livello di endpoint (vedi permessi-endpoint.properties), questa classe
 * verifica caso per caso che solo il proprietario o un ADMIN possano
 * consultarlo o modificarlo (vedi UtenteCorrente).
 */
@Service
public class InventarioService {

    private final InventarioRepository inventarioRepo;
    private final ArticoloInventarioRepository articoloRepo;
    private final InventarioMapper inventarioMapper;
    private final ArticoloInventarioMapper articoloMapper;
    private final ElementoCatalogoRepository elementoRepo;
    private final UtenteCorrente utenteCorrente;

    public InventarioService(
        InventarioRepository inventarioRepo,
        ArticoloInventarioRepository articoloRepo,
        InventarioMapper inventarioMapper,
        ArticoloInventarioMapper articoloMapper,
        ElementoCatalogoRepository elementoRepo,
        UtenteCorrente utenteCorrente
    ) {

        this.inventarioRepo = inventarioRepo;
        this.articoloRepo = articoloRepo;
        this.inventarioMapper = inventarioMapper;
        this.articoloMapper = articoloMapper;
        this.elementoRepo = elementoRepo;
        this.utenteCorrente = utenteCorrente;

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
     * @throws it.unipi.makermanagerserver.exception.AccessoNegatoException
     *         se l'inventario non appartiene all'utente corrente e non e' ADMIN
     */
    public List<ArticoloInventarioResponseDTO> trovaInventario(Long idInventario) {

        Inventario inventario = cercaInventarioDaId(idInventario);
        utenteCorrente.verificaProprietarioOAdmin(
            inventario.getUtente(),
            "Non puoi visualizzare un inventario che non ti appartiene"
        );

        return inventario.getArticoli()
                    .stream()
                    .map(articoloMapper::tResponseDTO)
                    .toList();

    }

    /**
     * Restituisce l'elenco degli inventari appartenenti a un utente.
     * Solo l'utente stesso o un ADMIN possono consultare questo elenco.
     *
     * @param idUtente id dell'utente di cui cercare gli inventari
     */
    public List<InventarioResponseDTO> trovaInventariUtente(Long idUtente) {

        utenteCorrente.verificaSeStessoOAdmin(
            idUtente,
            "Non puoi visualizzare gli inventari di un altro utente"
        );

        return inventarioRepo.findByUtenteId(idUtente)
                    .stream()
                    .map(inventarioMapper::toResponseDTO)
                    .toList();

    }

    /**
     * Crea un nuovo inventario a partire dal DTO di richiesta. Il
     * proprietario e' sempre l'utente autenticato che ha effettuato la
     * richiesta (vedi UtenteCorrente): non e' un dato scelto dal client.
     * 
     * @param dto dati del nuovo inventario (nome)
     * @return il DTO di risposta, comprensivo dell'id generato dal database
     */
    public InventarioResponseDTO creaInventario(InventarioRequestDTO dto) {

        Utente proprietario = utenteCorrente.get();
        Inventario inventario = inventarioMapper.toInventario(dto, proprietario);
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
     * @throws it.unipi.makermanagerserver.exception.AccessoNegatoException
     *         se l'inventario non appartiene all'utente corrente e non e' ADMIN
     */
    public void eliminaInventario(Long idInventario) {

        Inventario inventario = cercaInventarioDaId(idInventario);
        utenteCorrente.verificaProprietarioOAdmin(
            inventario.getUtente(),
            "Non puoi eliminare un inventario che non ti appartiene"
        );

        inventarioRepo.deleteById(idInventario);

    }

    /**
     * Crea un nuovo ArticoloInventario. L'inventario e l'elemento di catalogo
     * di destinazione sono indicati dagli id gia' presenti nel DTO
     * (ArticoloInventarioMapper.toArticoloInventario() li risolve internamente).
     *
     * @param dto dati del nuovo articolo (idElementoCatalogo, idInventario, quantita)
     * @return il DTO di risposta, comprensivo dell'id generato dal database
     * @throws it.unipi.makermanagerserver.exception.AccessoNegatoException
     *         se l'inventario di destinazione non appartiene all'utente corrente e non e' ADMIN
     */
    public ArticoloInventarioResponseDTO creaArticoloInventario(ArticoloInventarioRequestDTO dto) {

        ArticoloInventario articolo = articoloMapper.toArticoloInventario(dto);
        utenteCorrente.verificaProprietarioOAdmin(
            articolo.getInventario().getUtente(),
            "Non puoi aggiungere articoli a un inventario che non ti appartiene"
        );

        ArticoloInventario articoloDB = articoloRepo.save(articolo);

        return articoloMapper.tResponseDTO(articoloDB);

    }

    /**
     * Aggiorna la quantità di un articolo già presente in inventario,
     * sostituendo il valore esistente con quello indicato (non è un
     * incremento/decremento, ma un set assoluto).
     *
     * @param idArticolo id dell'articolo da aggiornare
     * @param dto        nuovo valore della quantità
     * @return il DTO di risposta aggiornato
     * @throws RisorsaNonTrovataException se l'articolo non esiste
     * @throws it.unipi.makermanagerserver.exception.AccessoNegatoException
     *         se l'inventario a cui appartiene l'articolo non appartiene
     *         all'utente corrente e non e' ADMIN
     */
    public ArticoloInventarioResponseDTO aggiornaQuantitaArticolo(
        Long idArticolo,
        AggiornaQuantitaDTO dto
    ) {

        ArticoloInventario articolo = articoloRepo.findById(idArticolo)
                .orElseThrow(() -> new RisorsaNonTrovataException(
                    "Articolo con id " + idArticolo + " non trovato"
                ));
        
        utenteCorrente.verificaProprietarioOAdmin(
            articolo.getInventario().getUtente(),
            "Non puoi modificare un articolo di un inventario che non ti appartiene"
        );

        articolo.setQuantita(dto.getQuantita());
        articoloRepo.save(articolo);

        return articoloMapper.tResponseDTO(articolo);

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
     * @throws it.unipi.makermanagerserver.exception.AccessoNegatoException
     *         se l'inventario a cui appartiene l'articolo non appartiene
     *         all'utente corrente e non e' ADMIN
     */
    public void eliminaArticoloDaInventario(Long idArticolo) {

        ArticoloInventario articolo = articoloRepo.findById(idArticolo)
                .orElseThrow(() -> new RisorsaNonTrovataException(
                    "Articolo con id " + idArticolo + " non trovato"
                ));

        utenteCorrente.verificaProprietarioOAdmin(
            articolo.getInventario().getUtente(),
            "Non puoi eliminare un articolo da un inventario che non ti appartiene"
        );

        articoloRepo.deleteById(idArticolo);

    }

    // ### Utility private ###

    private Inventario cercaInventarioDaId(Long idInventario) {

        return inventarioRepo.findById(idInventario)
                .orElseThrow(() -> new RisorsaNonTrovataException(
                    "Inventario con id " + idInventario + " non trovato"
                ));

    }

}