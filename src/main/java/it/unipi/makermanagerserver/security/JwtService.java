package it.unipi.makermanagerserver.security;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import it.unipi.makermanagerserver.enums.RuoloUtente;
import it.unipi.makermanagerserver.model.user.Utente;

/**
 * Genera e valida i token JWT usati per autenticare le richieste dopo il
 * login/registrazione. Il token contiene come subject l'email dell'utente
 * e come claim aggiuntivo il suo ruolo, cosi' JwtAuthFilter puo' ricostruire
 * le autorizzazioni senza dover interrogare nuovamente il database ad ogni
 * richiesta.
 */
@Component
public class JwtService {

    private final SecretKey chiaveFirma;
    private final long durataMs;

    public JwtService(
        @Value("${jwt.secret}") String secret,
        @Value("${jwt.expiration-ms}") long durataMs
    ) {

        this.chiaveFirma = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.durataMs = durataMs;

    }

    /**
     * Genera un nuovo token JWT firmato per l'utente indicato.
     */
    public String generaToken(Utente utente) {

        Date adesso = new Date();
        Date scadenza = new Date(adesso.getTime() + durataMs);

        return Jwts.builder()
                .subject(utente.getEmail())
                .claim("ruolo", utente.getRuolo().name())
                .issuedAt(adesso)
                .expiration(scadenza)
                .signWith(chiaveFirma)
                .compact();

    }

    public String estraiEmail(String token) {
        return estraiClaim(token, Claims::getSubject);
    }

    public RuoloUtente estraiRuolo(String token) {
        String ruolo = estraiClaim(token, claims -> claims.get("ruolo", String.class));
        return RuoloUtente.valueOf(ruolo);
    }

    /**
     * Un token e' valido solo se il subject corrisponde all'email attesa
     * (quella dell'utente appena caricato da DB) e non e' scaduto.
     */
    public boolean isTokenValido(String token, String emailAttesa) {

        String email = estraiEmail(token);
        return email.equals(emailAttesa) && !isTokenScaduto(token);

    }

    private boolean isTokenScaduto(String token) {
        return estraiClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T estraiClaim(String token, Function<Claims, T> resolver) {

        Claims claims = Jwts.parser()
                .verifyWith(chiaveFirma)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return resolver.apply(claims);

    }

}
