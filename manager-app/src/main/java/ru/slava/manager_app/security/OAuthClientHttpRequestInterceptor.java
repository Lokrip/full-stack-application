package ru.slava.manager_app.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;

//ClientHttpRequestInterceptor — это интерфейс в Spring Framework,
//который позволяет перехватывать и изменять
//HTTP-запросы перед их отправкой через RestTemplate. Он используется для выполнения различных действий,
//таких как добавление заголовков, логирование, обработка ошибок или модификация тела запроса.
//тоесть каждый http запрос через RestTemplate
//можно изменить например добавить загаловок Authorization Bearer token
public class OAuthClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    private final OAuth2AuthorizedClientManager authorizedClientManager;
    private final String registrationId;

    private SecurityContextHolderStrategy securityContextHolderStrategy =
        SecurityContextHolder.getContextHolderStrategy();

    public void setSecurityContextHolderStrategy(SecurityContextHolderStrategy securityContextHolderStrategy) {
        this.securityContextHolderStrategy = securityContextHolderStrategy;
    }

    public OAuthClientHttpRequestInterceptor(
        OAuth2AuthorizedClientManager authorizedClientManager,
        String registrationId
    ) {
        this.authorizedClientManager = authorizedClientManager;
        this.registrationId = registrationId;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            // authorize возвращает авторизованного клиента с токеном доступа
            OAuth2AuthorizedClient authorizedClient = this.authorizedClientManager.authorize(
                // указываем registrationId клиента, зарегистрированного в конфигурации Spring Security
                // по этому id система найдет настройки клиента (client_id, client_secret и т.д.)
                // и выполнит запрос на получение токена доступа
                OAuth2AuthorizeRequest.withClientRegistrationId(this.registrationId)
                    //нам надо передать principal связонные с этим ключом доступа
                    //нам необходим передать пользователя от иммени каторого
                    //мы будем запрашивать ключь доступа
                    .principal(this.securityContextHolderStrategy.getContext().getAuthentication())
                    .build());
            if(authorizedClient != null && authorizedClient.getAccessToken() != null) {
                request.getHeaders().setBearerAuth(authorizedClient.getAccessToken().getTokenValue());
            } else {
                throw new IllegalStateException("Failed to authorize client or retrieve access token.");
            }
        }

        return execution.execute(request, body);
    }

}
