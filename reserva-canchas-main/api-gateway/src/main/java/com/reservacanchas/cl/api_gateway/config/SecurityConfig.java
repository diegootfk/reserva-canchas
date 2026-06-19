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

                .pathMatchers(HttpMethod.GET,
                        "/usuarios",
                        "/usuarios/*")
                .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

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
                // SEDES
                // =========================

                .pathMatchers(HttpMethod.GET,
                        "/sedes",
                        "/sedes/*")
                .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                .pathMatchers(HttpMethod.POST,
                        "/sedes")
                .hasAuthority("ROLE_ADMIN")

                .pathMatchers(HttpMethod.PUT,
                        "/sedes/*")
                .hasAuthority("ROLE_ADMIN")

                .pathMatchers(HttpMethod.DELETE,
                        "/sedes/*")
                .hasAuthority("ROLE_ADMIN")

                // =========================
                // RESERVAS
                // =========================

                .pathMatchers(HttpMethod.GET,
                        "/reservas",
                        "/reservas/*",
                        "/reservas/estado/*",
                        "/reservas/usuario/*",
                        "/reservas/cancha/*")
                .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                .pathMatchers(HttpMethod.POST,
                        "/reservas")
                .hasAuthority("ROLE_ADMIN")

                .pathMatchers(HttpMethod.PUT,
                        "/reservas/*")
                .hasAuthority("ROLE_ADMIN")

                .pathMatchers(HttpMethod.DELETE,
                        "/reservas/*")
                .hasAuthority("ROLE_ADMIN")

                // =========================
                // CANCHAS
                // =========================

                .pathMatchers(HttpMethod.GET,
                        "/canchas",
                        "/canchas/*")
                .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                .pathMatchers(HttpMethod.POST,
                        "/canchas")
                .hasAuthority("ROLE_ADMIN")

                .pathMatchers(HttpMethod.PUT,
                        "/canchas/*")
                .hasAuthority("ROLE_ADMIN")

                .pathMatchers(HttpMethod.DELETE,
                        "/canchas/*")
                .hasAuthority("ROLE_ADMIN")

                // =========================
                // HORARIOS
                // =========================

                .pathMatchers(HttpMethod.GET,
                        "/horarios",
                        "/horarios/*")
                .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                .pathMatchers(HttpMethod.POST,
                        "/horarios")
                .hasAuthority("ROLE_ADMIN")

                .pathMatchers(HttpMethod.PUT,
                        "/horarios/*")
                .hasAuthority("ROLE_ADMIN")

                .pathMatchers(HttpMethod.DELETE,
                        "/horarios/*")
                .hasAuthority("ROLE_ADMIN")

                // =========================
                // DISPONIBILIDADES
                // =========================

                .pathMatchers(HttpMethod.GET,
                        "/disponibilidades",
                        "/disponibilidades/*")
                .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                .pathMatchers(HttpMethod.POST,
                        "/disponibilidades")
                .hasAuthority("ROLE_ADMIN")

                .pathMatchers(HttpMethod.PUT,
                        "/disponibilidades/*")
                .hasAuthority("ROLE_ADMIN")

                .pathMatchers(HttpMethod.DELETE,
                        "/disponibilidades/*")
                .hasAuthority("ROLE_ADMIN")

                // =========================
                // RESEÑAS
                // =========================

                .pathMatchers(HttpMethod.GET,
                        "/resenas",
                        "/resenas/*")
                .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                .pathMatchers(HttpMethod.POST,
                        "/resenas")
                .hasAuthority("ROLE_ADMIN")

                .pathMatchers(HttpMethod.PUT,
                        "/resenas/*")
                .hasAuthority("ROLE_ADMIN")

                .pathMatchers(HttpMethod.DELETE,
                        "/resenas/*")
                .hasAuthority("ROLE_ADMIN")

                // =========================
                // NOTIFICACIONES
                // =========================

                .pathMatchers(HttpMethod.GET,
                        "/notificaciones",
                        "/notificaciones/*")
                .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                .pathMatchers(HttpMethod.POST,
                        "/notificaciones")
                .hasAuthority("ROLE_ADMIN")

                .pathMatchers(HttpMethod.PUT,
                        "/notificaciones/*")
                .hasAuthority("ROLE_ADMIN")

                .pathMatchers(HttpMethod.DELETE,
                        "/notificaciones/*")
                .hasAuthority("ROLE_ADMIN")

                // =========================
                // MANTENIMIENTOS
                // =========================

                .pathMatchers("/mantenimientos/**")
                .hasAuthority("ROLE_ADMIN")

                // =========================
                // PAGOS
                // =========================

                .pathMatchers("/pagos/**")
                .hasAuthority("ROLE_ADMIN")

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