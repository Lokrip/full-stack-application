package ru.slava.manager_app.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestClient;

import ru.slava.manager_app.client.impl.ProductRestClientImpl;

@Configuration
public class ClientBean {


    @Bean
    public ProductRestClientImpl productRestClientImpl(
        @Value("${selmag.services.catalogue.uri:http://localhost:8081}") String catalogueBaseUri,
        @Value("${selmag.services.catalogue.username:}") String catalogueUsername,
        @Value("${selmag.services.catalogue.password:}") String cataloguePassword
    ) {
        return new ProductRestClientImpl(RestClient.builder()
            .baseUrl(catalogueBaseUri)
            .requestInterceptor(
                //в перехвачек добовляет basic аунтенфикацию
                //это нужно чтобы у нас были прова доступа к запросом
                new BasicAuthenticationInterceptor(
                    catalogueUsername,
                    cataloguePassword
                )
            )
            .build());
    }
}
