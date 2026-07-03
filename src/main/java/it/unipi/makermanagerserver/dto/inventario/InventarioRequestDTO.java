package it.unipi.makermanagerserver.dto.inventario;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

public class InventarioRequestDTO {
 
    @NotBlank(message = "Il nome dell'inventario è obbligatorio")
    private String nome;

    private Long idUtente;

    public InventarioRequestDTO() {
        
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(Long idUtente) {
        this.idUtente = idUtente;
    }

}
