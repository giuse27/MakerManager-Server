package it.unipi.makermanagerserver.security.permessi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Carica le regole di autorizzazione dal file esterno indicato dalla
 * proprieta' "permessi.file" (di default config/permessi-endpoint.properties,
 * vedi application.properties).
 *
 * Usa commons-configuration2 
 * il file viene ricontrollato ad ogni richiesta e ricaricato automaticamente
 * se modificato nel frattempo. Per cambiare i permessi non serve quindi
 * ricompilare ne' riavviare il server: basta modificare e salvare il file.
 */
@Component
public class PermessiEndpointRepository {

    private static final Pattern INDICE_REGOLA = Pattern.compile("regola\\.(\\d+)\\.");

    // il file viene controllato ogni secondo (motivo i test)
    // ricorda di impostare il valore a 5 s 
    private static final long RITARDO_RICONTROLLO_MS = 1000L;

    private final ReloadingFileBasedConfigurationBuilder<PropertiesConfiguration> builder;

    public PermessiEndpointRepository(@Value("${permessi.file}") String percorsoFile) {

        Parameters parametri = new Parameters();

        this.builder = new ReloadingFileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                .configure(parametri.properties()
                        .setFile(new File(percorsoFile))
                        .setListDelimiterHandler(new DefaultListDelimiterHandler(','))
                        .setReloadingRefreshDelay(RITARDO_RICONTROLLO_MS));

        // Fail-fast: se il file manca o e' malformato e' meglio bloccare
        // l'avvio del server con un errore chiaro, invece di scoprirlo alla
        // prima richiesta HTTP (che verrebbe rifiutata di default, vedi
        // PermessiEndpointAuthorizationManager).
        try {
            builder.getConfiguration();
        } catch (ConfigurationException e) {
            throw new IllegalStateException(
                "Impossibile leggere il file dei permessi endpoint: " + percorsoFile, e
            );
        }

    }

    /**
     * Restituisce le regole definite nel file, nell'ordine numerico dei
     * loro indici (regola.1, regola.2, ...): e' l'ordine di valutazione
     * usato da PermessiEndpointAuthorizationManager (prima regola che
     * corrisponde alla richiesta -> applicata, le successive ignorate).
     */
    public List<RegolaPermesso> trovaRegole() {

        try {

            // Controllo ottimizzato (solo data di ultima modifica del file):
            // se il file e' cambiato dall'ultima lettura, lo ricarica.
            builder.getReloadingController().checkForReloading(null);
            PropertiesConfiguration config = builder.getConfiguration();

            // ricostruisce l'elenco degli indici
            // tree set perché elimina i duplicati e ordina 1,2,3,...
            TreeSet<Integer> indici = new TreeSet<>();
            config.getKeys("regola").forEachRemaining(chiave -> {
                Matcher m = INDICE_REGOLA.matcher(chiave);
                if (m.find()) {
                    indici.add(Integer.parseInt(m.group(1)));
                }
            });

            // costruisce l'elenco delle regole tramite RegolaPermesso
            List<RegolaPermesso> regole = new ArrayList<>();
            for (int indice : indici) {

                String metodo = config.getString("regola." + indice + ".metodo");
                String pattern = config.getString("regola." + indice + ".path");
                List<String> ruoli = config.getList(String.class, "regola." + indice + ".ruoli");

                regole.add(new RegolaPermesso(metodo, pattern, ruoli));

            }

            return regole;

        } catch (ConfigurationException e) {
            throw new IllegalStateException(
                "Impossibile leggere il file dei permessi endpoint", e
            );
        }

    }

}
