package it.unipi.makermanagerserver.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.unipi.makermanagerserver.dto.init.CatalogoInitDTO;
import it.unipi.makermanagerserver.model.catalog.ElementoCatalogo;
import it.unipi.makermanagerserver.model.inventory.Inventario;
import it.unipi.makermanagerserver.repository.ArticoloInventarioRepository;
import it.unipi.makermanagerserver.repository.ElementoCatalogoRepository;
import it.unipi.makermanagerserver.repository.InventarioRepository;
import it.unipi.makermanagerserver.repository.ProgettoMakerRepository;
import tools.jackson.databind.ObjectMapper;

/**
 * Service dedicato alla logica di business dell'endpoint /inizializza
 * (SRP: questa classe si occupa solo di popolare/ripopolare il database,
 * nessun'altra responsabilita').
 *
 * Legge il file catalogo-iniziale.json da resources/data, e se il database
 * contiene gia' dati li cancella integralmente prima di ricaricare, come
 * richiesto dal documento di contesto del progetto (punto 4).
 */
@Service
public class InizializzazioneService {
    
    private static final Logger logger = LogManager.getLogger(InizializzazioneService.class);
    private static final String PERCORSO_JSON = "data/catalogo-iniziale.json";

    private final ElementoCatalogoRepository catalogoRepo;
    private final InventarioRepository inventarioRepo;
    private final ArticoloInventarioRepository articoloRepo;
    private final ProgettoMakerRepository progettoRepo;
    private final ObjectMapper objectMapper;
 
    // Iniezione delle dipendenze via costruttore: e' la modalita' raccomandata
    // in Spring (rispetto a @Autowired sui campi) perche' rende esplicite le
    // dipendenze della classe e permette di scrivere test unitari passando
    // dei mock senza dover avviare l'intero contesto Spring.
    public InizializzazioneService(
            ElementoCatalogoRepository catalogoRepo,
            InventarioRepository inventarioRepo,
            ArticoloInventarioRepository articoloRepo,
            ProgettoMakerRepository progettoRepo,
            ObjectMapper objectMapper
        ) 
    {
        this.catalogoRepo = catalogoRepo;
        this.inventarioRepo = inventarioRepo;
        this.articoloRepo = articoloRepo;
        this.progettoRepo = progettoRepo;
        this.objectMapper = objectMapper;
    }

    /**
     * Cancella tutti i dati esistenti e ricarica integralmente dal JSON.
     *
     * @Transactional: l'intera operazione viene eseguita come un'unica
     * transazione DB. Se qualcosa fallisce a meta' (es. un riferimento nel
     * JSON che non trova corrispondenza), Spring esegue il rollback
     * automatico e il database torna allo stato precedente, invece di
     * restare in uno stato parziale/inconsistente.
     */
    @Transactional
    private void inizializza() {

        logger.info("Avvio l'inizializzazione o la reinizializzazione del DB");

        CatalogoInitDTO dati = leggiJson();
        cancellaDatiEsistenti();

        // Inizializzazione vera e propria

        logger.info("Inizializzazione completata con successo");
    }

    // Lettura del file json
    private CatalogoInitDTO leggiJson() {

        try (InputStream inputStream = new ClassPathResource(PERCORSO_JSON).getInputStream()) {
            
            return objectMapper.readValue(inputStream, CatalogoInitDTO.class);

        } catch (IOException e) {
            
            // Un errore qui e' irrecuperabile per l'endpoint: senza il JSON
            // non c'e' nulla da inizializzare. Lo trasformiamo in una
            // RuntimeException non controllata, che il controller potra'
            // intercettare per restituire un errore HTTP appropriato.
            throw new IllegalStateException("Impossibile leggere il file di inizializzazione: " + PERCORSO_JSON, e);

        }
    }

    // Cancellazione dei dati esistenti
    /*
     * Ordine di cancellazione obbligato dai vincoli delle foreign key:
     * 1) ProgettoMaker: il cascade su BOM.righeFabbisogno cancella anche le RigaBOM
     * 2) Inventario: il cascade su Inventario.articoli cancella anche gli ArticoloInventario
     * 3) ElementoCatalogo: solo ora e' sicuro, nessuno lo referenzia piu'
     */
    private void cancellaDatiEsistenti() {

        logger.info("Sto cancellando i dati esistenti");
        progettoRepo.deleteAll();
        inventarioRepo.deleteAll();
        catalogoRepo.deleteAll();

    }

    // Caricamento di ElementoCatalogo

    // Caricamento di Inventario

    // Caricamento di ArticoloInventario (dipende da ElementoCatalogo e da Inventario)

    /*
     * Factory minimale per istanziare la sottoclasse corretta di ArticoloInventario
     * sulla base del campo "tipo" del json
     */

    // Caricamento di ProgettoMaker + BOM (dipende da ElementoCatalogo)

    /*
     * Factory minimale per istanziare la sottoclasse corretta di ProgettoMaker
     * sulla base del campo "tipo" del json
     */

    // Creazione della BOM

    // Utility per risoluzione di riferimenti testuali

        private ElementoCatalogo risolviElementoCatalogo(Map<String, ElementoCatalogo> catalogoPerNome, String nome) {
        ElementoCatalogo elemento = catalogoPerNome.get(nome);
        if (elemento == null) {
            throw new IllegalStateException(
                    "Riferimento non valido nel JSON di inizializzazione: ElementoCatalogo '" + nome + "' non trovato.");
        }
        return elemento;
    }
 
    private Inventario risolviInventario(Map<String, Inventario> inventariPerNome, String nome) {
        Inventario inventario = inventariPerNome.get(nome);
        if (inventario == null) {
            throw new IllegalStateException(
                    "Riferimento non valido nel JSON di inizializzazione: Inventario '" + nome + "' non trovato.");
        }
        return inventario;
    }

}
