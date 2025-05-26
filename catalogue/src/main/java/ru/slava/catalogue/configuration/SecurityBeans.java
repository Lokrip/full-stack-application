 package ru.slava.catalogue.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityBeans {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeRequests(authorizeRequests -> authorizeRequests
                // .requestMatchers(HttpMethod.POST, "/catalogue-api/products")
                //     //указываем для каждого полномочние префикс scope потому что он в keycloak сщитаеться scope
                //     //keycloak автоматический добовляет префикс SCOPE_
                //     .hasAuthority("SCOPE_edit_catalogue")
                // .requestMatchers(HttpMethod.PATCH, "/catalogue-api/products/{productId:\\d}")
                //     .hasAuthority("SCOPE_edit_catalogue")
                // .requestMatchers(HttpMethod.DELETE, "/catalogue-api/products/{productId:\\d}")
                //     .hasAuthority("SCOPE_edit_catalogue")
                // //проверяем если у каждого GET запроса есть полномачние view_catalogue
                // //то авторизуем пользователя
                // .requestMatchers(HttpMethod.GET)
                //     .hasAuthority("SCOPE_view_catalogue")
                // //anyRequest denyAll значит все остальнные запросы для всех отклоняються
                // .anyRequest().denyAll()
                .anyRequest().permitAll()
            )
            .csrf(CsrfConfigurer::disable)
            .sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            //Говорит, что ресурсный сервер будет использовать JWT для проверки токенов.
            //Spring сам подгрузит публичный ключ из issuer-uri, чтобы верифицировать подпись токена.
            .oauth2ResourceServer(oauth2ResourceServer ->
                oauth2ResourceServer.jwt(Customizer.withDefaults()))
            .build();
    }
}
