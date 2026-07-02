package it.unipi.makermanagerserver;

import it.unipi.makermanagerserver.model.catalog.ElementoCatalogo;
import it.unipi.makermanagerserver.enums.TipologiaElemento;
import it.unipi.makermanagerserver.repository.ElementoCatalogoRepository;
import it.unipi.makermanagerserver.repository.ArticoloInventarioRepository;
import it.unipi.makermanagerserver.repository.ProgettoMakerRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MakerManagerServerApplication {

    private static final Logger logger = LogManager.getLogger(MakerManagerServerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MakerManagerServerApplication.class, args);
    }

    /*
     * Testbench di avvio: verifica che la connessione al database 
     * e l'inizializzazione dei bean JPA avvengano con successo.
     * * Nota: Utilizziamo un controllo di esistenza preventiva per evitare inserimenti
     * duplicati causati dall'avvio congiunto dei Test JUnit e del Server reale.
     */
    @Bean
    public CommandLineRunner eseguiTestbench(
            ElementoCatalogoRepository catalogoRepo,
            ArticoloInventarioRepository inventarioRepo,
            ProgettoMakerRepository progettiRepo) {
            
        return args -> {
            logger.info("=================================================");
            logger.info("🚀 AVVIO TESTBENCH DEL SERVER MAKERMANAGER v0.0.0");
            logger.info("=================================================");

            try {
                String nomeElemento = "Raspberry Pi 4 Model B";

                // 1. Verifichiamo se l'elemento esiste già nel catalogo prima di salvare
                if (catalogoRepo.findByNomeContainingIgnoreCase(nomeElemento).isEmpty()) {
                    ElementoCatalogo raspberry = new ElementoCatalogo();
                    raspberry.setNome(nomeElemento);
                    raspberry.setDescrizione("Single Board Computer 4GB RAM");
                    raspberry.setTipologia(TipologiaElemento.COMPONENTE_ELETTRONICO);

                    catalogoRepo.save(raspberry);
                    logger.info("✅ Elemento Catalogo inserito con successo! ID: " + raspberry.getId());
                } else {
                    logger.info("L'elemento '" + nomeElemento + "' è già presente nel DB. Salto l'inserimento per evitare duplicati.");
                }

                // 2. Eseguiamo un controllo di conteggio per validare la persistenza
                long conteggioCatalogo = catalogoRepo.count();
                logger.info("📊 Elementi totali nel Catalogo: " + conteggioCatalogo);
                
                // 3. Verifichiamo i metodi di query personalizzati del repository inventario
                int sogliaScorte = 5;
                long articoliSottoSoglia = inventarioRepo.findByQuantitaLessThanEqual(sogliaScorte).size();
                logger.info("📦 Articoli in esaurimento (scorte <= " + sogliaScorte + "): " + articoliSottoSoglia);

                logger.info("🎉 TESTBENCH SUPERATO CON SUCCESSO! Lo schema JPA è integro.");
            } catch (Exception e) {
                logger.error("❌ ERRORE DURANTE IL TESTBENCH DEI REPOSITORY: ", e);
            }
            logger.info("=================================================");
        };
    }
}