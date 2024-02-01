package br.com.microservices.statelessanyapi.service;

import br.com.microservices.statelessanyapi.dto.AuthUserResponse;
import br.com.microservices.statelessanyapi.exception.ValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
@RequiredArgsConstructor
public class JwtService {
    private static final Integer HOURS = 24;
    private static final String EMPTY_SPACE = " ";
    private static final Integer TOKEN_INDEX = 1;


    @Value("${app.token.secret-key}")
    private String secretKey;


    public AuthUserResponse getAuthenticatedUser(String token) {
        var tokenClaims = getClaims(token);
        var userId = Integer.valueOf((String) tokenClaims.get("id"));
        return new AuthUserResponse(userId, (String) tokenClaims.get("username"));
    }

    public void validateAccessToken(String token) {
        getClaims(token);
    }

    private Claims getClaims(String token) {
        var accessToken = extractToken(token);
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(generateSign())
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (Exception e) {
            throw new ValidationException("Invalid token" + e.getMessage());
        }
    }

    public String extractToken(String token) {
        if (token.isEmpty()) {
            throw new ValidationException("Token was not informed");
        }
        return token.contains(EMPTY_SPACE) ? token.split(EMPTY_SPACE)[TOKEN_INDEX] : token;
    }
    private SecretKey generateSign() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

}
