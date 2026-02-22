package ru.otus.courses.java.advanced.shooter.server.gateway.admin.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String BEARER_AUTHORIZATION = "bearerAuth";

       @Bean
    public OpenAPI openAPI() {

        return new OpenAPI()
                .info(
                        new Info()
                                .title("API Gateway for admin")
                                .version("v1")
                )
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        BEARER_AUTHORIZATION,
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(BEARER_AUTHORIZATION)
                );
    }
}