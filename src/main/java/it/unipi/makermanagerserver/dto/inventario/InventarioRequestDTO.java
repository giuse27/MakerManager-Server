package it.unipi.makermanagerserver.dto.inventario;

import jakarta.validation.constraints.NotBlank;

public class InventarioRequestDTO {
 
    @NotBlank(message = "Il nome dell'inventario è obbligatorio")
    private String nome;

    public InventarioRequestDTO() {
        
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}