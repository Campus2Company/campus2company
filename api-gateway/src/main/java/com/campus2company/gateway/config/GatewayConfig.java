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
                        .path("/auth/**")
                        .uri("http://auth-service:8080"))
                .route("student-service", r -> r
                        .path("/students/**")
                        .uri("http://student-service:8080"))
                .route("employer-service", r -> r
                        .path("/employers/**")
                        .uri("http://employer-service:8080"))
                .route("lecturer-service", r -> r
                        .path("/lecturers/**")
                        .uri("http://lecturer-service:8080"))
                .route("university-admin-service", r -> r
                        .path("/university-admins/**")
                        .uri("http://university-admin-service:8080"))
                .route("admin-service", r -> r
                        .path("/admin-ops/**")
                        .uri("http://admin-service:8080"))
                .route("project-service", r -> r
                        .path("/projects/**")
                        .uri("http://project-service:8080"))
                .route("application-service", r -> r
                        .path("/applications/**")
                        .uri("http://application-service:8080"))
                .route("messaging-service", r -> r
                        .path("/messages/**")
                        .uri("http://messaging-service:8080"))
                .route("notification-service", r -> r
                        .path("/notifications/**")
                        .uri("http://notification-service:8080"))
                .build();
    }
}