package br.com.microservices.statelessanyapi.service;

import br.com.microservices.statelessanyapi.dto.AnyResponse;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AnyService {

    private final JwtService jwtService;

    public AnyResponse getData(String accessToken) {
        jwtService.validateAccessToken(accessToken);
        var authUser = jwtService.getAuthenticatedUser(accessToken);
        var ok = HttpStatus.OK;
        return new AnyResponse(ok.name(), ok.value(), authUser);
    }

    public AnyResponse sumNumbers(int a, int b, String accessToken) {
        jwtService.validateAccessToken(accessToken);
        var ok = HttpStatus.OK;
        var sum = a + b;
        return new AnyResponse(ok.name(), ok.value(), sum);
    }
}
