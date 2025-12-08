package com.luis.drakdex.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration; // <--- Importante

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License; // <--- Importante
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    // Lê a URL do Railway das variáveis (ou usa localhost se não existir)
    @Value("${railway.public.url:http://localhost:8080}")
    private String railwayUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("DrakDex API")
                .version("1.0")
                .description("Documentação oficial do Bestiário de RPG DrakDex")
                .termsOfService("http://swagger.io/terms/")
                .license(new License().name("Apache 2.0").url("http://springdoc.org")))
            .servers(List.of(
                new Server().url(railwayUrl).description("Servidor Principal"),
                new Server().url("http://localhost:8080").description("Servidor Local")
            ));
    }
}