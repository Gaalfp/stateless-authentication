package br.com.microservices.statelessauthapi.core.service;

import br.com.microservices.statelessauthapi.core.model.User;
import br.com.microservices.statelessauthapi.infra.exception.AuthenticationException;
import br.com.microservices.statelessauthapi.infra.exception.ValidationException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class JwtService {

    private static final Integer HOURS = 24;
    private static final String EMPTY_SPACE = " ";
    private static final Integer TOKEN_INDEX = 1;


    @Value("${app.token.secret-key}")
    private String secretKey;

    public String createToken(User user) {
        var data = new HashMap<String, String>();
        data.put("id", user.getId().toString());
        data.put("username", user.getUsername());
        return Jwts.builder()
                .setClaims(data)
                .setExpiration(generateExpiresAt())
                .signWith(generateSign())
                .compact();
    }

    private Date generateExpiresAt() {
        return Date.from(LocalDateTime.now()
                .plusHours(HOURS)
                .atZone(ZoneId.systemDefault()).toInstant()
        );
    }

    private SecretKey generateSign() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }


    public void validateAccessToken(String token) {
        var accessToken = extractToken(token);
        try {
            Jwts
                    .parserBuilder()
                    .setSigningKey(generateSign())
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (Exception e) {
            throw new AuthenticationException("Invalid token" + e.getMessage());
        }
    }

    public String extractToken(String token) {
        if (token.isEmpty()) {
            throw new ValidationException("Token was not informed");
        }
        return token.contains(EMPTY_SPACE) ? token.split(EMPTY_SPACE)[TOKEN_INDEX] : token;
    }
}
