package com.luis.drakdex.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.luis.drakdex.model.Usuario;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    // 1. GERAR TOKEN (Quando o usuário faz login)
    public String generateToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("drakdex-api") // Quem emitiu
                    .withSubject(usuario.getEmail()) // Quem é o dono do token (identificador único)
                    .withExpiresAt(genExpirationDate()) // Quando expira
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    // 2. VALIDAR TOKEN (Quando o usuário tenta acessar algo)
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("drakdex-api")
                    .build()
                    .verify(token)
                    .getSubject(); // Retorna o email se estiver tudo ok
        } catch (JWTVerificationException exception) {
            return ""; // Retorna vazio se for inválido
        }
    }

    // Define que o token expira em 24 horas (ajusta conforme quiseres)
    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(24).toInstant(ZoneOffset.of("-03:00"));
    }
}