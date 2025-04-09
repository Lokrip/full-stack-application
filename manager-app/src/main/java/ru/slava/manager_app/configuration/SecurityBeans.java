package ru.slava.manager_app.configuration;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityBeans {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            //authorizeRequests: Этот метод конфигурирует авторизацию запросов
            //anyRequest(): Указывает, что правило авторизации применяется ко всем запросам
            //authenticated(): Указывает, что для доступа к любому запросу, который проходит через это правило,
            //необходимо быть аутентифицированным пользователем. То есть, пользователь должен быть авторизован
            //(например, через логин и пароль), чтобы получить доступ к любому ресурсу на сервере.
            .authorizeRequests(authorizeRequests -> {
                System.out.println("------------------------------yes---------------------------------------");
                authorizeRequests.anyRequest().hasRole("MANAGER");
            })
            .oauth2Login(Customizer.withDefaults())
            .build();
    }

    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oAuth2UserService() {
        OidcUserService oidcUserService = new OidcUserService();
        return userRequest -> {
            OidcUser oidcUser = oidcUserService.loadUser(userRequest);
            List<SimpleGrantedAuthority> authorities = Optional.ofNullable(oidcUser.getClaimAsStringList("groups"))
                .orElseGet(List::of)
                .stream()
                .filter(role -> role.startsWith("ROLE_"))
                .map(SimpleGrantedAuthority::new)
                .toList();
            return new DefaultOidcUser(authorities, oidcUser.getIdToken());
        };
    }
}
