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

	// Questo Bean viene eseguito in automatico da Spring Boot all'avvio
    // Spring Boot inietterà automaticamente i TUOI repository
    @Bean
    public CommandLineRunner eseguiTestbench(
            ElementoCatalogoRepository catalogoRepo,
            ArticoloInventarioRepository inventarioRepo,
            ProgettoMakerRepository progettiRepo) {
            
        return args -> {
            logger.info("=== INIZIO TEST DEI REPOSITORY (v0.0.0-feature-testbench) ===");

            try {
                // 1. Creiamo ed inseriamo un elemento teorico nel catalogo
                ElementoCatalogo raspberry = new ElementoCatalogo();
                raspberry.setNome("Raspberry Pi 4 Model B");
                raspberry.setDescrizione("Single Board Computer 4GB RAM");
                raspberry.setTipologia(TipologiaElemento.COMPONENTE_ELETTRONICO);

                catalogoRepo.save(raspberry);
                logger.info("✅ Elemento Catalogo inserito con successo! ID: " + raspberry.getId());

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