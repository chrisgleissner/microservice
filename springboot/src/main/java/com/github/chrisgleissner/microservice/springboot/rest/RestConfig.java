package com.github.chrisgleissner.microservice.springboot.rest;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import java.util.Optional;

@Configuration @RequiredArgsConstructor @Slf4j
public class RestConfig {
    private final Optional<BuildProperties> buildProperties;
    private final Environment environment;

    @Bean
    public OpenAPI openApi() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        ))
                .info(new Info()
                        .title("Spring Boot Microservice REST API")
                        .version(buildProperties.map(p -> String.format("%s  -  Build time %s", p.getVersion(), p.getTime())).orElse("")));
    }

    @EventListener(ApplicationReadyEvent.class)
    public void ready() {
        log.info("API docs at http://localhost:{}/micro/swagger-ui.html", environment.getProperty("local.server.port"));
    }
}
