package br.com.microservices.statelessauthapi.core.service;

import br.com.microservices.statelessauthapi.core.dto.AuthRequest;
import br.com.microservices.statelessauthapi.core.dto.TokenDTO;
import br.com.microservices.statelessauthapi.infra.exception.ValidationException;
import br.com.microservices.statelessauthapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public TokenDTO login(AuthRequest authRequest) {
        var user = userRepository.findByUsername(authRequest.username())
                .orElseThrow(() -> new ValidationException("User not found"));
        var accessToken = jwtService.createToken(user);
        validatePassword(authRequest.password(), user.getPassword());
        return new TokenDTO(accessToken);
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new ValidationException("The password is invalid");
        }
    }

    public TokenDTO validateToken(String accessToken) {
        validateExistingToken(accessToken);
        jwtService.validateAccessToken(accessToken);
        return new TokenDTO(accessToken);
    }

    private void validateExistingToken(String accessToken) {
        if(accessToken.isEmpty()) {
            throw new ValidationException("Token was not informed");
        }

    }
}
