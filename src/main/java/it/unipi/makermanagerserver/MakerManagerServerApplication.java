package it.unipi.makermanagerserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import it.unipi.makermanagerserver.enums.TipologiaElemento;
import it.unipi.makermanagerserver.model.catalog.ElementoCatalogo;
import it.unipi.makermanagerserver.repository.ArticoloInventarioRepository;
import it.unipi.makermanagerserver.repository.ElementoCatalogoRepository;
import it.unipi.makermanagerserver.repository.ProgettoMakerRepository;

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

            // 1. Instanziamo un elemento teorico per il Catalogo
            ElementoCatalogo arduino = new ElementoCatalogo();
            arduino.setNome("Arduino Uno R3");
            
            arduino.setTipologia(TipologiaElemento.COMPONENTE_ELETTRONICO); 
            
            // 2. Salvataggio nel Database
            catalogoRepo.save(arduino);
            logger.info("✅ Dati salvati con successo. ID assegnato da MySQL: " + arduino.getId());

            // 3. Test generico di conteggio per assicurarsi che le tabelle rispondano
            long elementiInCatalogo = catalogoRepo.count();
            logger.info("📊 Elementi totali presenti nel DB (Tabella Catalogo): " + elementiInCatalogo);

            logger.info("=== FINE TEST ===");
        };
    }

}
