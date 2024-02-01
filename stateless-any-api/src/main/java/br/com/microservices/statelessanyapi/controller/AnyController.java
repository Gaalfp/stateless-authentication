package br.com.microservices.statelessanyapi.controller;

import br.com.microservices.statelessanyapi.dto.AnyResponse;
import br.com.microservices.statelessanyapi.service.AnyService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/any")
public class AnyController {

    private final AnyService anyService;

    @GetMapping("/getdata")
    public AnyResponse getData(@RequestHeader String accessToken) {
        return anyService.getData(accessToken);
    }

    @GetMapping("/sumnumbers")
    public AnyResponse sumNumbers(@RequestHeader String accessToken, int a, int b) {
        return anyService.sumNumbers(a, b, accessToken);
    }
}
