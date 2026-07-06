package it.unipi.makermanagerserver.dto.auth;

/**
 * DTO di RISPOSTA per registrazione e login: oltre ai dati essenziali
 * dell'utente autenticato include il token JWT da usare nell'header
 * "Authorization: Bearer <token>" per le richieste successive.
 */
public class AuthResponseDTO {

    private String token;
    private Long id;
    private String nickname;
    private String email;
    private String ruolo;

    public AuthResponseDTO() {
    }

    public AuthResponseDTO(
        String token, Long id, String nickname, String email, String ruolo
    ) {
        this.token = token;
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.ruolo = ruolo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
