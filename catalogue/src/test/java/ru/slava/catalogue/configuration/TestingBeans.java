package ru.slava.catalogue.configuration;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@Configuration
public class TestingBeans {


    // Мокаем (подменяем) экземпляр класса JwtDecoder,
    // потому что в тестах у нас нет resource server,
    // который в продакшене используется для декодирования JWT-токенов,
    // приходящих от клиентов. Этот бин нужен для контекста приложения,
    // чтобы тесты, использующие безопасность (Spring Security), могли
    // успешно запускаться без реального декодирования JWT.
    @Bean
    public JwtDecoder jwtDecoder() {
        return mock(JwtDecoder.class);
    }
}
