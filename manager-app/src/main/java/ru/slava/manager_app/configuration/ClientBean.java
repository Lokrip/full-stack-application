package ru.slava.manager_app.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import ru.slava.manager_app.client.ProductRestClient;
import ru.slava.manager_app.client.impl.ProductRestClientImpl;

@Configuration
public class ClientBean {


    @Bean
    public ProductRestClientImpl productRestClientImpl(
        @Value("${selmag.service.catalogue.uri:http://localhost:8081}") String catalogueBaseUri
    ) {
        return new ProductRestClientImpl(RestClient.builder()
            .baseUrl(catalogueBaseUri)
            .build());
    }
}
