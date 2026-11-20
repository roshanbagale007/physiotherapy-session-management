package com.physio.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Unit 4.1: Spring Boot 3.3 Microservice Bootstrap.
 * Demonstrates:
 * - @SpringBootApplication: Combines @Configuration, @EnableAutoConfiguration, and @ComponentScan.
 * - Embedded Tomcat server running independently on port 8080.
 * - Auto-configured Spring Data JPA, HikariCP, Hibernate 6, and Jackson JSON serializer.
 */
@SpringBootApplication
public class ApiMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiMicroserviceApplication.class, args);
    }
}
