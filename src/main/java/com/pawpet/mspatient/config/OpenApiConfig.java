package com.pawpet.mspatient.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI pawPetOpenAPI() {
        Server server = new Server();
        server.setUrl("http://localhost:" + serverPort);
        server.setDescription("Servidor de desarrollo");

        Contact contact = new Contact();
        contact.setEmail("contact@pawpet.com");
        contact.setName("PawPet Team");

        License license = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Microservicio de Pacientes API")
                .version("1.0")
                .contact(contact)
                .description("API para la gestión de pacientes, mascotas y registros médicos del sistema PawPet")
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }
}
