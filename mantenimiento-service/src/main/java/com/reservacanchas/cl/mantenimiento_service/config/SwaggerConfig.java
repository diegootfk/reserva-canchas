package com.reservacanchas.cl.mantenimiento_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        Server gatewayServer = new Server();
        gatewayServer.setUrl("http://localhost:7090");
        gatewayServer.setDescription("API Gateway");

        return new OpenAPI()
                .servers(List.of(gatewayServer))
                .info(new Info()
                        .title("API Mantenimiento")
                        .version("1.0")
                        .description("Documentación del microservicio Mantenimiento"));
    }
}