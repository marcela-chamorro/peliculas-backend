package com.unrn.peliculas.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.*;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Almacén de Películas API",
                version = "1.0",
                description = "API para el sistema de gestión de películas online"
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.OAUTH2,
        bearerFormat = "JWT",
        scheme = "bearer",
        flows = @OAuthFlows(
                password = @OAuthFlow(
                        tokenUrl = "${KEYCLOAK_ISSUER_URI:http://localhost:9090/realms/cinecloud}/protocol/openid-connect/token",
                        scopes = {
                                @OAuthScope(name = "openid", description = "OpenID scope"),
                                @OAuthScope(name = "profile", description = "Profile scope")
                        }
                )
        )
)
public class OpenApiConfig {
}
