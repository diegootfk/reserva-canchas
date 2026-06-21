package com.reservacanchas.cl.api_gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;

import org.springframework.core.Ordered;

import org.springframework.http.server.reactive.ServerHttpRequest;

import org.springframework.stereotype.Component;

import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class LoggingGlobalFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(LoggingGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        long startTime = System.currentTimeMillis();

        String method = request.getMethod() != null
                ? request.getMethod().name()
                : "UNKNOWN";

        String path = request.getURI().getPath();

        String remoteAddress = request.getRemoteAddress() != null
                ? request.getRemoteAddress().toString()
                : "UNKNOWN";

        logger.info(
                "API Gateway recibió solicitud: metodo={}, path={}, origen={}",
                method,
                path,
                remoteAddress
        );

        return chain.filter(exchange)
                .doOnSuccess(aVoid -> {
                    long duration = System.currentTimeMillis() - startTime;

                    logger.info(
                            "API Gateway respondió solicitud: metodo={}, path={}, status={}, tiempo={}ms",
                            method,
                            path,
                            exchange.getResponse().getStatusCode(),
                            duration
                    );
                })
                .doOnError(error -> {
                    long duration = System.currentTimeMillis() - startTime;

                    logger.error(
                            "Error en API Gateway: metodo={}, path={}, tiempo={}ms, mensaje={}",
                            method,
                            path,
                            duration,
                            error.getMessage(),
                            error
                    );
                });
    }

    @Override
    public int getOrder() {
        return -1;
    } 
}