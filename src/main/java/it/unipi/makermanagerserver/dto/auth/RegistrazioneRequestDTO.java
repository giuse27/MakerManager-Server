package it.unipi.makermanagerserver.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO di RICHIESTA: rappresenta i dati che il client invia per registrare
 * un nuovo Utente.
 *
 * Non contiene il ruolo: ogni nuovo utente registrato riceve sempre e solo
 * il ruolo UTENTE
 */
public class RegistrazioneRequestDTO {

    @NotBlank(message = "Il nickname è obbligatorio")
    private String nickname;

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "L'email non è valida")
    private String email;

    @NotBlank(message = "La password è obbligatoria")
    @Size(min = 8, message = "La password deve contenere almeno 8 caratteri")
    private String password;

    public RegistrazioneRequestDTO() {
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
