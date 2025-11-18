package com.watchcrew.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
  
  @Value("${server.port:8080}")
  private String serverPort;
  
  @Bean
  public OpenAPI watchCrewOpenAPI() {
    Server localServer = new Server();
    localServer.setUrl("http://localhost:" + serverPort);
    localServer.setDescription("Local Development Server");
    
    Contact contact = new Contact();
    contact.setName("WatchCrew Team");
    contact.setEmail("support@watchcrew.com");
    
    License mitLicense = new License()
        .name("MIT License")
        .url("https://opensource.org/licenses/MIT");
    
    Info info = new Info()
        .title("WatchCrew API")
        .version("1.0.0")
        .description("Movie recommendation and social platform API built with Spring Boot and Hexagonal Architecture")
        .contact(contact)
        .license(mitLicense);
    
    // Define JWT Bearer security scheme
    SecurityScheme securityScheme = new SecurityScheme()
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT")
        .in(SecurityScheme.In.HEADER)
        .name("Authorization")
        .description("JWT Bearer token authentication. Format: Bearer {token}");
    
    // Add security requirement
    SecurityRequirement securityRequirement = new SecurityRequirement()
        .addList("bearerAuth");
    
    return new OpenAPI()
        .info(info)
        .servers(List.of(localServer))
        .components(new Components()
            .addSecuritySchemes("bearerAuth", securityScheme))
        .addSecurityItem(securityRequirement);
  }
  
  @Bean
  public GroupedOpenApi authenticationApi() {
    return GroupedOpenApi.builder()
        .group("authentication")
        .displayName("Authentication API")
        .pathsToMatch("/api/auth/**")
        .build();
  }

  @Bean
  public GroupedOpenApi usersApi() {
    return GroupedOpenApi.builder()
        .group("users")
        .displayName("Users API")
        .pathsToMatch("/api/users/**")
        .build();
  }

  @Bean
  public GroupedOpenApi moviesApi() {
    return GroupedOpenApi.builder()
        .group("movies")
        .displayName("Movies API")
        .pathsToMatch("/api/movies/**")
        .build();
  }

  @Bean
  public GroupedOpenApi ratingsApi() {
    return GroupedOpenApi.builder()
        .group("ratings")
        .displayName("Ratings API")
        .pathsToMatch("/api/ratings/**")
        .build();
  }

  @Bean
  public GroupedOpenApi reviewsApi() {
    return GroupedOpenApi.builder()
        .group("reviews")
        .displayName("Reviews API")
        .pathsToMatch("/api/reviews/**")
        .build();
  }

  @Bean
  public GroupedOpenApi recommendationsApi() {
    return GroupedOpenApi.builder()
        .group("recommendations")
        .displayName("Recommendations API")
        .pathsToMatch("/api/recommendations/**")
        .build();
  }

  @Bean
  public GroupedOpenApi movieListsApi() {
    return GroupedOpenApi.builder()
        .group("movie-lists")
        .displayName("Movie Lists API")
        .pathsToMatch("/api/movie-lists/**")
        .build();
  }

  @Bean
  public GroupedOpenApi messagesApi() {
    return GroupedOpenApi.builder()
        .group("messages")
        .displayName("Messages API")
        .pathsToMatch("/api/messages/**")
        .build();
  }
}
