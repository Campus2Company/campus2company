package com.campus2company.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/api/auth/**")
                        .uri("http://auth-service:8080"))
                .route("user-service", r -> r
                        .path("/api/users/**")
                        .uri("http://user-service:8080"))
                .route("message-service", r -> r
                        .path("/api/messages/**")
                        .uri("http://message-service:8080"))
                .route("notification-service", r -> r
                        .path("/api/notifications/**")
                        .uri("http://notification-service:8080"))
                .route("project-service", r -> r
                        .path("/api/projects/**")
                        .uri("http://project-service:8080"))
                .route("application-service", r -> r
                        .path("/api/applications/**")
                        .uri("http://application-service:8080"))
                .build();
    }
}