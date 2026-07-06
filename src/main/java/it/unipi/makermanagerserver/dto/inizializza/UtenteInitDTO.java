package it.unipi.makermanagerserver.dto.inizializza;

/**
 * UtenteInitDTO è il dto di caricamento degli utenti da inzializzazione.json
 * rappresenta la struttura di quel pezzo di json in cui viene definito l'utente
 * NOTA: gli utenti creati in inizializza hanno sempre ruolo UTENTE, gli admin
 * possono essere impostati manualmente dal database e ce n'è almeno uno hard
 * coded all'avvio del server (io XD)
 */
public class UtenteInitDTO {

    private String nickname;
    private String email;
    private String password;

    public UtenteInitDTO() {

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
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
}
