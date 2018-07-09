package com.spring.cloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
@SpringBootApplication
public class GatewayRun {
    public static void main(String[] args) {
        SpringApplication.run(GatewayRun.class, args);
    }

//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return
//                builder.routes()
//                        .route(t -> t.path("/slowRequest").and().uri("http://localhost:8021"))
//                        .route(t -> t.path("/bigRequest").and().uri("http://localhost:8021"))
//                        .route(t -> t.path("/hello").and().uri("http://localhost:8021"))
//                        .build();
//    }

}
