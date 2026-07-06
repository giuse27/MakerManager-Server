package it.unipi.makermanagerserver.dto.inventario;

public class InventarioResponseDTO {
    
    private Long id;
    private String nome;
    private Long idUtente;
    private String nicknameUtente;

    public InventarioResponseDTO() {

    }
    
    public InventarioResponseDTO(Long id, String nome, Long idUtente, String nicknameUtente) {
        this.id = id;
        this.nome = nome;
        this.idUtente = idUtente;
        this.nicknameUtente = nicknameUtente;
    }

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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

    public String getNicknameUtente() {
        return nicknameUtente;
    }

    public void setNicknameUtente(String nicknameUtente) {
        this.nicknameUtente = nicknameUtente;
    }

}
