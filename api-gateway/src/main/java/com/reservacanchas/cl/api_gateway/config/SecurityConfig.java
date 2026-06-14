package com.reservacanchas.cl.api_gateway.config;

import java.nio.charset.StandardCharsets;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.secret-key}")
    private String secret;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http
                .csrf(csrf -> csrf.disable())

                .authorizeExchange(exchanges -> exchanges

                        // AUTH
                        .pathMatchers("/auth/**").permitAll()

                        // SWAGGER Y OPENAPI
                        .pathMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/v3/api-docs/**",
                                "/api-docs/**"
                        ).permitAll()

                        // =========================
                        // USUARIOS
                        // =========================

                        // USER o ADMIN
                        .pathMatchers(HttpMethod.GET,
                                "/usuarios",
                                "/usuarios/*")
                        .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                        // SOLO ADMIN
                        .pathMatchers(HttpMethod.POST,
                                "/usuarios")
                        .hasAuthority("ROLE_ADMIN")

                        .pathMatchers(HttpMethod.PUT,
                                "/usuarios/*")
                        .hasAuthority("ROLE_ADMIN")

                        .pathMatchers(HttpMethod.DELETE,
                                "/usuarios/*")
                        .hasAuthority("ROLE_ADMIN")

                        // =========================
                        // PAGOS
                        // =========================

                        .pathMatchers("/pagos/**")
                        .hasAuthority("ROLE_ADMIN")

                        // RESTO DE MICROSERVICIOS
                        .anyExchange().authenticated()
                )

                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )

                .build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {

        SecretKeySpec key = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                "HmacSHA384"
        );

        return NimbusReactiveJwtDecoder
                .withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS384)
                .build();
    }

    @Bean
    public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {

        ReactiveJwtAuthenticationConverter converter =
                new ReactiveJwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {

            String role = jwt.getClaimAsString("role");

            if (role == null) {
                return reactor.core.publisher.Flux.empty();
            }

            return reactor.core.publisher.Flux.just(
                    new org.springframework.security.core.authority.SimpleGrantedAuthority(
                            "ROLE_" + role
                    )
            );
        });

        return converter;
    }
}