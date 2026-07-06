package it.unipi.makermanagerserver.dto.auth;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO di RICHIESTA: credenziali inviate dal client per autenticarsi
 */
public class LoginRequestDTO {

    @NotBlank(message = "L'email è obbligatoria")
    private String email;

    @NotBlank(message = "La password è obbligatoria")
    private String password;

    public LoginRequestDTO() {
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
