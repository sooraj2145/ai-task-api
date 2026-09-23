package com.sooraj.aitaskapi.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI taskApiOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                .title("AI Task API")
                .version("1.0.0")
                        .description(
                                "A production-oriented task management REST API " +
                                "built with Spring Boot, PostgreSQL and JPA. " +
                                "The API provides task management, validation, " +
                                "filtering, searching, sorting and pagination."
                        ));
    }
}
