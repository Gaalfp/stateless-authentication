package br.com.microservices.statelessauthapi.core.dto;

public record AuthRequest (String username, String password) {
}
