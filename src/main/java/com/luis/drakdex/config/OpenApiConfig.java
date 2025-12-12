package com.luis.drakdex.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Value("${SERVER_URL:http://localhost:8080}")
    private String serverUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("DrakDex API")
                .version("1.0")
                .description("Documentação oficial do Bestiário de RPG DrakDex")
                .license(new License().name("Apache 2.0").url("http://springdoc.org")))
            .servers(List.of(
                // Garante que o teu link do Render está aqui
                new Server().url("https://drakdex-api.onrender.com").description("Servidor de Produção"),
                new Server().url("http://localhost:8080").description("Servidor Local")
            ))
            // --- A MÁGICA ACONTECE AQUI ---
            // 1. Dizemos que toda a API precisa de segurança
            .addSecurityItem(new SecurityRequirement().addList("bearer-key"))
            // 2. Definimos como é essa segurança (Token JWT)
            .components(new Components()
                .addSecuritySchemes("bearer-key",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
    }
}