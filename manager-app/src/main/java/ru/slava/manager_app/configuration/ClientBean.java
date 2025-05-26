package ru.slava.manager_app.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;

import ru.slava.manager_app.client.impl.ProductRestClientImpl;
import ru.slava.manager_app.security.OAuthClientHttpRequestInterceptor;

@Configuration
public class ClientBean {


    // @Bean
    // public ProductRestClientImpl productRestClientImpl(
    //     @Value("${selmag.services.catalogue.uri:http://localhost:8081}") String catalogueBaseUri,
    //     @Value("${selmag.services.catalogue.username:}") String catalogueUsername,
    //     @Value("${selmag.services.catalogue.password:}") String cataloguePassword
    // ) {
    //     return new ProductRestClientImpl(RestClient.builder()
    //         .baseUrl(catalogueBaseUri)
    //         .requestInterceptor(
    //             //в перехвачек добовляет basic аунтенфикацию
    //             //это нужно чтобы у нас были прова доступа к запросом
    //             new BasicAuthenticationInterceptor(
    //                 catalogueUsername,
    //                 cataloguePassword
    //             )
    //         )
    //         .build());
    // }


    @Bean
    public ProductRestClientImpl productRestClientImpl(
        @Value("${selmag.services.catalogue.uri:http://localhost:8081}") String catalogueBaseUri,
        ClientRegistrationRepository clientRegistrationRepository,
        OAuth2AuthorizedClientRepository authorizedClientRepository,
        @Value("${selmag.services.catalogue.registration-id:keycloak}") String registrationId
    ) {
        return new ProductRestClientImpl(RestClient.builder()
            .baseUrl(catalogueBaseUri)
            .requestInterceptor(
               new OAuthClientHttpRequestInterceptor(
                    //есть два класса
                    //DefaultOAuth2AuthorizedClientManager и AuthorizedClientServiceOAuth2AuthorizedClientManager
                    //AuthorizedClientServiceOAuth2AuthorizedClientManager данные манаджер арентирован
                    //вне контекста пользователя тоесть когды вы используете client credentials client_credentials —
                    //это один из грантов (grant types) в OAuth 2.0. Он используется для машинного взаимодействия —
                    //то есть, когда один сервис (или приложение) обращается к другому от имени самого себя, а не пользователя.
                    //тоесть при обьчном меж сервисном взаимодействии или когда контекст пользователя нас
                    //не интересует для этого нужен AuthorizedClientServiceOAuth2AuthorizedClientManager
                    //DefaultOAuth2AuthorizedClientManager данные манаджер арентирован на использование
                    //в контексте пользователя

                    //AuthorizedClientServiceOAuth2AuthorizedClientManager — это как раз тот самый инструмент,
                    //который реализует межсервисную авторизацию в Spring Security.
                    //DefaultOAuth2AuthorizedClientManager — это менеджер пользовательской авторизации.
                    //Пользователь аутентифицирован (например, через форму логина или OAuth2 login),
                    //И ты хочешь от его имени выполнять запросы к другим защищённым ресурсам (например, к API стороннего сервиса, как GitHub, Google Drive и т.д.).
                    new DefaultOAuth2AuthorizedClientManager(
                        //этот класс требует ClientRegistrationRepository
                        //и OAuth2AuthorizedClientRepository
                        //ClientRegistrationRepository - Это интерфейс, который Хранит информацию о зарегистрированных клиентах OAuth2
                        //OAuth2AuthorizedClientRepository - Это интерфейс, который хранит информацию о авторизованных клиентах OAuth2
                        clientRegistrationRepository,
                        authorizedClientRepository
                    ),
                    registrationId
               )
            )
            .build());
    }
}


/*

🔑 ClientRegistrationRepository
Назначение:
Хранит конфигурацию OAuth2 клиентов.

Пример:
В конфигурации приложения вы можете задать несколько OAuth2 клиентов (например, Google, GitHub, Keycloak), и каждый будет описан как ClientRegistration.

Содержит:

client-id

client-secret

scopes

redirect-uri

token-uri

authorization-uri

и т.д.

Используется для:
Когда Spring Security нужно понять, как подключаться к конкретному OAuth2 провайдеру, он берёт данные из ClientRegistrationRepository.

🔐 OAuth2AuthorizedClientRepository
Назначение:
Хранит информацию об уже авторизованных клиентах — то есть, клиентов, которые прошли авторизацию и получили токены.

Пример:
Когда пользователь уже авторизовался через OAuth2 (или когда сервис уже получил access_token через client_credentials), информация о клиенте и его токенах сохраняется здесь.

Содержит:

access token

refresh token

клиент, от имени которого был получен токен

контекст пользователя (если есть)

Используется для:
Чтобы повторно использовать токены, обновлять их (через refresh token), и не запрашивать заново авторизацию каждый раз.

В связке:
DefaultOAuth2AuthorizedClientManager использует оба этих компонента:

Из ClientRegistrationRepository он берёт настройки клиента (client-id, scopes и т.д.)

С OAuth2AuthorizedClientRepository он работает для хранения и извлечения уже авторизованных клиентов и токенов

Когда это нужно?
Для межсервисной авторизации (client_credentials) — это важно, чтобы правильно управлять токенами.

Для пользовательской авторизации (authorization_code, password) — чтобы выполнять запросы от имени пользователя после входа.



*/
