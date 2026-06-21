package com.reservacanchas.cl.api_gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiGatewayApplication {

    private static final Logger logger = LoggerFactory.getLogger(ApiGatewayApplication.class);

    public static void main(String[] args) {
        logger.info("Iniciando API Gateway");
        SpringApplication.run(ApiGatewayApplication.class, args);
        logger.info("API Gateway iniciado correctamente");
    }
}