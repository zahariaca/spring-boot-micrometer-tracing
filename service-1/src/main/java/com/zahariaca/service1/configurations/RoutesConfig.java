package com.zahariaca.service1.configurations;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutesConfig {
    @Bean
    public RouteLocator customRouteLocator(final RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route("route-to-2nd-service",
                        route -> route.path("/second/cloud-gateway")
                                .uri("http://127.0.0.1:8081"))
                .build();
    }
}
