package it.unipi.makermanagerserver.dto.utente;

/**
 * DTO di RISPOSTA: rappresenta i dati pubblici di un Utente
 * Non contiene mai la password, nemmeno criptata.
 */
public class UtenteResponseDTO {

    private Long id;
    private String nickname;
    private String email;
    private String ruolo;

    public UtenteResponseDTO() {
    }

    public UtenteResponseDTO(Long id, String nickname, String email, String ruolo) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.ruolo = ruolo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

}
