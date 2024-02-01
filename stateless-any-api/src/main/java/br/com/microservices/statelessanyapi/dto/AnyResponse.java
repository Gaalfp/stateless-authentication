package br.com.microservices.statelessanyapi.dto;

public record AnyResponse(String status, int code, Object object) {
}
