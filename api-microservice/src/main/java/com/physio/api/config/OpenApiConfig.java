package com.physio.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Unit 4.2: Springdoc OpenAPI 3.0 & Swagger UI Configuration.
 * Exposes interactive API documentation and testing dashboard at /swagger-ui.html.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI physiotherapyOpenAPI() {
        Contact contact = new Contact();
        contact.setName("ROSHAN NAMDEO BAGALE (Roll No: 50 | Div D | 24MCAC302)");
        contact.setEmail("roshan.bagale@mituniversity.edu.in");
        contact.setUrl("https://github.com/roshanbagale007/physiotherapy-session-management");

        License license = new License()
                .name("MIT-ADT University MCA Evaluation License")
                .url("https://mituniversity.ac.in");

        Info info = new Info()
                .title("Physiotherapy Session Management & Clinical Rehabilitation REST API")
                .version("1.0.0")
                .description("""
                    Production-grade healthcare microservice for physiotherapy clinics and rehabilitation centers.
                    
                    **Features:**
                    - Conflict-free slot scheduling & dynamic booking with package credit deduction.
                    - Real-time therapist slot availability calculation.
                    - Attendance tracking (SCHEDULED, ATTENDED, CANCELLED, NO_SHOW).
                    - Digital clinical SOAP progress notes with Visual Analog Scale (VAS) pain score delta tracking.
                    - Clinical protocol adherence & recovery KPI analytics computed via Spring Data JPA.
                    """)
                .contact(contact)
                .license(license);

        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Local Development Microservice Server (Port 8080)");

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
}
