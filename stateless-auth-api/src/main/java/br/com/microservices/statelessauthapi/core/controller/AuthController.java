package br.com.microservices.statelessauthapi.core.controller;

import br.com.microservices.statelessauthapi.core.dto.AuthRequest;
import br.com.microservices.statelessauthapi.core.dto.TokenDTO;
import br.com.microservices.statelessauthapi.core.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public TokenDTO login(@RequestBody AuthRequest authRequest) {
        return authService.login(authRequest);
    }

    @PostMapping("/validateToken")
    public TokenDTO validateToken(@RequestHeader String token) {
        return authService.validateToken(token);
    }


}
