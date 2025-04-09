package ru.slava.catalogue.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityBeans {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorizeHttpRequests ->
                    authorizeHttpRequests.requestMatchers("/catalogue-api/**")
                        //через hasAuthority Проверяет, есть ли у пользователя конкретное полномочие "ROLE_SERVICE".
                        //В Spring Security полномочия (authorities) — это строки, которые могут представлять роли или другие права доступа.
                        // .hasAuthority("ROLE_SERVICE")
                        //В Spring Security роли автоматически конвертируются в полномочия, добавляя префикс "ROLE_".
                        //То есть .hasRole("SERVICE") эквивалентно .hasAuthority("ROLE_SERVICE").
                        //через hasRole мы проверяем пользователя роль SERVICE если да то даем доступ
                        .hasRole("SERVICE"))
                    //включаем basic аунтефикацию
                    .httpBasic(Customizer.withDefaults())
                    //отлючаем сессию аунтенфикаций в цепочке фильтрах для вастонавления http сессий
                    .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .build();
    }
}
