package it.unipi.makermanagerserver.service.endpoint;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.unipi.makermanagerserver.dto.inizializza.ArticoloInventarioInitDTO;
import it.unipi.makermanagerserver.dto.inizializza.CatalogoInitDTO;
import it.unipi.makermanagerserver.dto.inizializza.ElementoCatalogoInitDTO;
import it.unipi.makermanagerserver.dto.inizializza.InventarioInitDTO;
import it.unipi.makermanagerserver.dto.inizializza.ProgettoInitDTO;
import it.unipi.makermanagerserver.dto.inizializza.RigaBOMInitDTO;
import it.unipi.makermanagerserver.dto.inizializza.UtenteInitDTO;
import it.unipi.makermanagerserver.enums.RuoloUtente;
import it.unipi.makermanagerserver.enums.TipologiaElemento;
import it.unipi.makermanagerserver.enums.TipologiaProgetto;
import it.unipi.makermanagerserver.exception.DatiNonValidiException;
import it.unipi.makermanagerserver.factory.ArticoloInventarioFactory;
import it.unipi.makermanagerserver.factory.ProgettoMakerFactory;
import it.unipi.makermanagerserver.model.catalog.ElementoCatalogo;
import it.unipi.makermanagerserver.model.inventory.ArticoloInventario;
import it.unipi.makermanagerserver.model.inventory.Inventario;
import it.unipi.makermanagerserver.model.project.BOM;
import it.unipi.makermanagerserver.model.project.ProgettoMaker;
import it.unipi.makermanagerserver.model.project.RigaBOM;
import it.unipi.makermanagerserver.model.user.Utente;
import it.unipi.makermanagerserver.repository.ArticoloInventarioRepository;
import it.unipi.makermanagerserver.repository.ElementoCatalogoRepository;
import it.unipi.makermanagerserver.repository.InventarioRepository;
import it.unipi.makermanagerserver.repository.ProgettoMakerRepository;
import it.unipi.makermanagerserver.repository.UtenteRepository;
import tools.jackson.databind.ObjectMapper;

/**
 * Service dedicato alla logica di business dell'endpoint /inizializza
 * (SRP: questa classe si occupa solo di popolare/ripopolare il database,
 * nessun'altra responsabilita').
 *
 * Legge il file inzializzazione.json da resources/data, e se il database
 * contiene gia' dati li cancella integralmente prima di ricaricare, come
 * richiesto dal documento di contesto del progetto (punto 4).
 */
@Service
public class InizializzazioneService {
    
    private static final Logger logger = LogManager.getLogger(InizializzazioneService.class);
    private static final String PERCORSO_JSON = "data/inizializzazione.json";

    private final ElementoCatalogoRepository catalogoRepo;
    private final InventarioRepository inventarioRepo;
    private final ArticoloInventarioRepository articoloRepo;
    private final ProgettoMakerRepository progettoRepo;
    private final UtenteRepository utenteRepo;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final String adminDefaultNickname;

    // Iniezione delle dipendenze via costruttore: e' la modalita' raccomandata
    // in Spring (rispetto a @Autowired sui campi) perche' rende esplicite le
    // dipendenze della classe e permette di scrivere test unitari passando
    // dei mock senza dover avviare l'intero contesto Spring.
    public InizializzazioneService(
            ElementoCatalogoRepository catalogoRepo,
            InventarioRepository inventarioRepo,
            ArticoloInventarioRepository articoloRepo,
            ProgettoMakerRepository progettoRepo,
            UtenteRepository utenteRepo,
            ObjectMapper objectMapper,
            PasswordEncoder passwordEncoder,
            @Value("${admin.default.nickname}") String adminDefaultNickname
        )
    {
        this.catalogoRepo = catalogoRepo;
        this.inventarioRepo = inventarioRepo;
        this.articoloRepo = articoloRepo;
        this.progettoRepo = progettoRepo;
        this.utenteRepo = utenteRepo;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
        this.adminDefaultNickname = adminDefaultNickname;
    }

    /**
     * Cancella tutti i dati esistenti e ricarica integralmente dal JSON.
     *
     * Annotazione @Transactional: l'intera operazione viene eseguita come 
     * un'unica transazione DB. Se qualcosa fallisce a meta' (es. un riferimento 
     * nel JSON che non trova corrispondenza), Spring esegue il rollback
     * automatico e il database torna allo stato precedente, invece di
     * restare in uno stato parziale/inconsistente.
     */
    @Transactional
    public void inizializza() {

        logger.info("Avvio l'inizializzazione o la reinizializzazione del DB");

        CatalogoInitDTO dati = leggiJson();
        cancellaDatiEsistenti();

        // Inizializzazione vera e propria
        Map<String, Utente> utentiPerNickname = caricaUtenti(dati);
        Map<String, ElementoCatalogo> catalogoPerNome = caricaCatalogo(dati);
        Map<String, Inventario> inventariPerNome = caricaInventario(dati, utentiPerNickname);
        caricaArticoliInventario(dati, catalogoPerNome, inventariPerNome);
        caricaProgetti(dati, catalogoPerNome, utentiPerNickname);        

        logger.info("Inizializzazione completata con successo");
    }

    // Cancellazione dei dati esistenti
    /**
     * Ordine di cancellazione obbligato dai vincoli delle foreign key:
     * 1) ProgettoMaker: il cascade su BOM.righeFabbisogno cancella anche le RigaBOM
     * 2) Inventario: il cascade su Inventario.articoli cancella anche gli ArticoloInventario
     * 3) ElementoCatalogo: solo ora e' sicuro, nessuno lo referenzia piu'
     * 4) Utente: solo dopo 1 e 2 e vengono eliminati tutti gli utenti tranne admin default
     */
    private void cancellaDatiEsistenti() {

        logger.info("Sto cancellando i dati esistenti");
        progettoRepo.deleteAll();
        inventarioRepo.deleteAll();
        catalogoRepo.deleteAll();
        utenteRepo.deleteAllByNicknameNot(adminDefaultNickname);

        /*
         * Le delete derivate di Spring Data (deleteAll/deleteAllByNicknameNot)
         * si limitano a REGISTRARE le rimozioni nel contesto di persistenza:
         * le istruzioni DELETE fisiche vengono eseguite solo al prossimo
         * flush, secondo l'ordine di flush "normale" di Hibernate. Le
         * entita' con generazione IDENTITY (Utente compreso) devono pero'
         * eseguire il loro INSERT immediatamente al momento del save (e'
         * l'unico modo per ottenere subito l'id generato dal DB), scavalcando
         * quell'ordine. Senza un flush esplicito qui, ricaricare un Utente
         * con la stessa email/nickname di uno appena "cancellato" (esattamente
         * il caso di un /inizializza richiamato su un sistema gia' inizializzato)
         * fallisce con un vincolo di unicita' violato, perche' la vecchia riga
         * e' ancora fisicamente presente sul DB quando parte il nuovo INSERT.
         * Un flush su un singolo repository esegue comunque tutte le
         * operazioni pendenti dell'intero contesto di persistenza (stessa
         * transazione, stesso EntityManager), quindi basta chiamarlo una volta.
         */
        utenteRepo.flush();

    }

    // Caricamento di ElementoCatalogo
    private Map<String, ElementoCatalogo> caricaCatalogo(CatalogoInitDTO dati) {

        Map<String, ElementoCatalogo> res = new HashMap<>();

        for (ElementoCatalogoInitDTO dto : elencoSicuro(dati.getCatalogo())) {

            ElementoCatalogo elemento = new ElementoCatalogo(
                dto.getNome(), 
                dto.getDescrizione(), 
                risolviTipologiaElemento(dto.getTipologia())
            );
            catalogoRepo.save(elemento);
            res.put(dto.getNome(), elemento);

        }

        logger.info("Caricati {} elementi in catalogo", res.size());
        return res;

    }

    // Caricamento di Inventario
    private Map<String, Inventario> caricaInventario(
        CatalogoInitDTO dati, 
        Map<String, Utente> utentiPerNickname
    ) {

        Map<String, Inventario> res = new HashMap<>();

        for (InventarioInitDTO dto : elencoSicuro(dati.getInventari())) {

            Utente utente = risolviUtente(utentiPerNickname, dto.getUtente());

            Inventario inventario = new Inventario(
                dto.getNome(), 
                utente
            );
            inventarioRepo.save(inventario);
            res.put(dto.getNome(), inventario);

        }

        logger.info("Caricati {} inventari", res.size());
        return res;

    }

    // Caricamento di ArticoloInventario (dipende da ElementoCatalogo e da Inventario)
    private void caricaArticoliInventario(
        CatalogoInitDTO dati,
        Map<String, ElementoCatalogo> catalogoPerNome,
        Map<String, Inventario> inventarioPerNome
    ) {

        int count = 0;

        for (ArticoloInventarioInitDTO dto : elencoSicuro(dati.getArticoliInventario())) {

            ElementoCatalogo elemento = risolviElementoCatalogo(catalogoPerNome, dto.getElementoCatalogo());
            Inventario inventario = risolviInventario(inventarioPerNome, dto.getInventario());

            ArticoloInventario articolo = ArticoloInventarioFactory.creaArticoloInventario(
                dto.getTipo(), elemento, inventario, dto.getQuantita()
            );

            // aggiungiArticolo mantiene coerenti entrambi i lati della relazione
            // bidirezionale (vedi Inventario.aggiungiArticolo)
            inventario.aggiungiArticolo(articolo);
            articoloRepo.save(articolo);
            count++;

        }

        logger.info("Caricati {} articoli dell'inventario", count);

    }

    // Caricamento di ProgettoMaker + BOM (dipende da ElementoCatalogo)
    private void caricaProgetti(
        CatalogoInitDTO dati, 
        Map<String, ElementoCatalogo> catalogoPerNome,
        Map<String, Utente> utentiPerNickname
    ) {

        int count = 0;

        for (ProgettoInitDTO dto : elencoSicuro(dati.getProgetti())) {

            TipologiaProgetto tipologia = risolviTipologiaProgetto(dto.getTipo());

            ProgettoMaker progetto = ProgettoMakerFactory.creaProgetto(tipologia);

            progetto.setNome(dto.getNome());
            progetto.setDescrizione(dto.getDescrizione());
            progetto.setAutore(risolviUtente(utentiPerNickname, dto.getAutore()));
            progetto.setTipologia(tipologia);
            progetto.setDistintaBase(creaBOM(dto.getBom(), catalogoPerNome));

            progettoRepo.save(progetto);
            count++;

        }

        logger.info("Caricati {} progetti", count);

    }

    // Creazione della BOM
    private BOM creaBOM(List<RigaBOMInitDTO> righeDto, Map<String, ElementoCatalogo> catalogoPerNome) {
        
        BOM bom = new BOM();

        for (RigaBOMInitDTO rigaDto : elencoSicuro(righeDto)) {
            
            ElementoCatalogo elemento = risolviElementoCatalogo(catalogoPerNome, rigaDto.getElementoCatalogo());
            bom.aggiungiRiga(new RigaBOM(elemento, rigaDto.getQuantita()));

        }

        return bom;

    }

    // Caricamento degli utenti
    private Map<String, Utente> caricaUtenti(CatalogoInitDTO dati) {

        Map<String, Utente> res = new HashMap<>();

        for (UtenteInitDTO dto : elencoSicuro(dati.getUtenti())) {

            // La password nel JSON e' in chiaro (comoda da scrivere/leggere
            // per i dati di test): va sempre criptata con BCrypt prima di
            // salvarla, esattamente come fara' AuthService in fase di
            // registrazione. Non va mai persistita in chiaro sul DB.
            String passwordCriptata = passwordEncoder.encode(dto.getPassword());

            Utente utente = new Utente(
                dto.getNickname(), 
                dto.getEmail(), 
                passwordCriptata, 
                RuoloUtente.UTENTE
            );
            utenteRepo.save(utente);
            res.put(dto.getNickname(), utente);

        }

        logger.info("Caricati {} utenti", res.size());
        return res;

    }

    // #########################################################################
    // ------------------------- UTILITY PRIVATE -------------------------------
    // #########################################################################

    // ### Utility per la lettura del file JSON
    private CatalogoInitDTO leggiJson() {

        try (InputStream inputStream = new ClassPathResource(PERCORSO_JSON).getInputStream()) {
            
            return objectMapper.readValue(inputStream, CatalogoInitDTO.class);

        } catch (IOException e) {
            
            /**
             * Un errore qui e' irrecuperabile per l'endpoint: senza il JSON
             * non c'e' nulla da inizializzare. Lo trasformiamo in una
             * RuntimeException non controllata, che il GlobalExceptionHandler
             * intercettera' con l'handler di fallback restituendo un 500:
             * e' corretto che sia un 500 (non un errore dell'utente/client,
             * ma una configurazione mancante lato server).
            */
            throw new IllegalStateException("Impossibile leggere il file di inizializzazione: " + PERCORSO_JSON, e);

        }
    }

    // ### Utility per liste potenzialmente assenti nel JSON ###

    /**
     * Se una sezione del JSON di inizializzazione viene omessa (es. il
     * file non contiene affatto la chiave "progetti"), Jackson lascia il
     * campo corrispondente a null invece di una lista vuota. Questo
     * metodo evita NullPointerException nei cicli for-each, trattando
     * una sezione assente come "nessun elemento da caricare" invece che
     * come un errore.
     */
    private <T> List<T> elencoSicuro(List<T> lista) {
        return lista != null ? lista : List.of();
    }

    // ### Utility per risoluzione di riferimenti testuali ###

    private TipologiaElemento risolviTipologiaElemento(String tipologia) {
        try {
            return TipologiaElemento.valueOf(tipologia);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new DatiNonValidiException(
                "Tipologia '" + tipologia + "' non valida nel JSON di inizializzazione. Valori ammessi: "
                + java.util.Arrays.toString(TipologiaElemento.values())
            );
        }
    }

    private TipologiaProgetto risolviTipologiaProgetto(String tipologia) {
        try {
            return TipologiaProgetto.valueOf(tipologia);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new DatiNonValidiException(
                "Tipologia '" + tipologia + "' non valida nel JSON di inizializzazione. Valori ammessi: "
                + java.util.Arrays.toString(TipologiaProgetto.values())
            );
        }
    }

    private ElementoCatalogo risolviElementoCatalogo(Map<String, ElementoCatalogo> catalogoPerNome, String nome) {
        ElementoCatalogo elemento = catalogoPerNome.get(nome);
        if (elemento == null) {
            throw new DatiNonValidiException(
                    "Riferimento non valido nel JSON di inizializzazione: ElementoCatalogo '" + nome + "' non trovato.");
        }
        return elemento;
    }
 
    private Inventario risolviInventario(Map<String, Inventario> inventariPerNome, String nome) {
        Inventario inventario = inventariPerNome.get(nome);
        if (inventario == null) {
            throw new DatiNonValidiException(
                    "Riferimento non valido nel JSON di inizializzazione: Inventario '" + nome + "' non trovato.");
        }
        return inventario;
    }

    /**
     * verifica che un utente esista a partire dal suo nickname
     */
    private Utente risolviUtente(Map<String, Utente> utentiPerNickname, String nickname) {
        Utente utente = utentiPerNickname.get(nickname);
        if (utente == null) {
            throw new DatiNonValidiException(
                    "Riferimento non valido nel JSON di inizializzazione: Utente '" + nickname + "' non trovato.");
        }
        return utente;
    }

}