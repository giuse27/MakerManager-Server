package it.unipi.makermanagerserver.dto.catalogo;

/**
 * DTO di RISPOSTA: rappresenta i dati che il server restituisce al client
 * per un ElementoCatalogo (GET /api/catalogo, e come esito di POST).
 *
 * A differenza del RequestDTO, include l'id: qui l'entity esiste gia'
 * nel database, quindi il client ne ha bisogno per riferirsi ad essa
 * in operazioni successive (es. DELETE /api/catalogo/{id}).
 */
public class ElementoCatalogoResponseDTO {

    private Long id;
    private String nome;
    private String descrizione;
    private String tipologia;

    public ElementoCatalogoResponseDTO() {
    }

    public ElementoCatalogoResponseDTO(Long id, String nome, String descrizione, String tipologia) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.tipologia = tipologia;
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

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }
    
}