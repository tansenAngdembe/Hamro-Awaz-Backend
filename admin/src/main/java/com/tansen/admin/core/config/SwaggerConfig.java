package com.tansen.admin.core.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info().title("HAMRO AWAZ SUPER ADMIN PANEL REST APIs DOCUMENTATION"));
//                .addSecurityItem(new SecurityRequirement().addList("CosmoRentalSecurityScheme"));
//                .components(new Components().addSecuritySchemes("CosmoRentalSecurityScheme", new SecurityScheme()
//                        .name("CosmoRentalSecurityScheme").type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
}
