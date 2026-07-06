package it.unipi.makermanagerserver.model.user;

import it.unipi.makermanagerserver.enums.RuoloUtente;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Modello di Utente. Utente è un account registrato su MakerManager
 * 
 * Utente ha un Ruolo che può essere UTENTE o ADMIN e in base al ruolo ottiene i
 * permessi per poter effettuare determinate azioni
 * 
 * TODO i permessi devono essere configurabili
 */
@Entity
@Table(name = "utente")
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    // la password viene criptata con BCrypt, mai salvata o restituita in chiaro
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuoloUtente ruolo;

    // costruttori

    public Utente() {
    
    }

    public Utente(String nickname, String email, String password, RuoloUtente ruolo) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
    }

    // getter e setter

    public Long getId() {
        return id;
    }

    // set id non presente perché l'id lo genera id db

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

    public RuoloUtente getRuolo() {
        return ruolo;
    }

    public void setRuolo(RuoloUtente ruolo) {
        this.ruolo = ruolo;
    }

}