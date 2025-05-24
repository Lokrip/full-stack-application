package ru.slava.customer_app.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import ru.slava.customer_app.client.impl.WebClientProductClient;

@Configuration
public class ClientConfig {

    @Bean
    public WebClientProductClient webClientProductClient(
        @Value("${selmag.services.catalogue.uri:http://localhost:8081}") String catalogueBaseUrl
    ) {
        return new WebClientProductClient(WebClient.builder()
            .baseUrl(catalogueBaseUrl)
            .build());
    }
}
