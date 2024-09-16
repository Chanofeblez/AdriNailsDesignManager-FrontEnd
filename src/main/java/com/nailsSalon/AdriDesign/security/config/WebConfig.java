package com.nailsSalon.AdriDesign.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                        "http://localhost:8100",       // Desarrollo local con Ionic
                        "http://localhost:4200",       // Desarrollo local con Angular
                        "https://adrinailsdesign-c393e5baf34a.herokuapp.com",  // Tu aplicación en Heroku
                        "capacitor://localhost"        // Para accesos desde dispositivos móviles usando Capacitor
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
