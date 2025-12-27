package com.tansen.app.core.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info().title("HAMRO AWAZ APP REST APIs DOCUMENTATION"))
                .addSecurityItem(new SecurityRequirement().addList("HamroAwazSecurityScheme"))
                .components(new Components().addSecuritySchemes("HamroAwazSecurityScheme", new SecurityScheme()
                        .name("HamroAwazSecurityScheme").type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
}
