package ru.slava.manager_app.configuration;

import static org.mockito.Mockito.mock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;

import ru.slava.manager_app.client.ProductRestClient;
import ru.slava.manager_app.client.impl.ProductRestClientImpl;

@Configuration
public class TestingBeans {

    //эти мок объекты нужны потому что в итеграционных тестах мы будем отпровлять запрос на сервер
    //и нам нужно в DefaultOAuth2AuthorizedClientManager передать бины clientRegistrationRepository
    //и бин oAuth2AuthorizedClientRepository так как их в тестах нету нету будут выдовать ошибку
    //без них мы не сможем взять client_id, client_secret с clientRegistrationRepository
    //и авторизованных клиентов и токенов в oAuth2AuthorizedClientRepository


    // Создаем мок-объект ClientRegistrationRepository для использования в тестах
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return mock(ClientRegistrationRepository.class);
    }

    @Bean
    public OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository() {
        return mock(OAuth2AuthorizedClientRepository.class);
    }


    @Bean
    //чтобы по время тестов иммено этот компонент использовался
    //как главные нужно укозать Primary
    //Primary делает данный Bean главнным кандидатом
    //на внедрения для ProductRestClient
    @Primary
    public ProductRestClient testProductRestClient(
        @Value("${selmag.services.catalogue.uri:http://localhost:54321}") String catalogueBaseUri
    ) {
        return new ProductRestClientImpl(RestClient.builder()
            .baseUrl(catalogueBaseUri)
            .build());
    }
}
