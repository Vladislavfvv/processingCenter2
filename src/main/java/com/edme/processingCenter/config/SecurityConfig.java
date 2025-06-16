package com.edme.processingCenter.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity //поддержка аннотаций безопасности
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults()) // Включаем CORS из WebConfig
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/auth").permitAll()
                                .requestMatchers(
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/webjars/**",
                                        "/swagger-resources/**"
                                ).permitAll()
                                // Только GET для роли "user"
                                .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("user", "admin")
                                // Все методы для роли "admin"
                                .requestMatchers("/api/**").hasRole("admin")
                                // // Остальные запросы требуют аутентификацию
                                .anyRequest().authenticated()
                        //SessionCreationPolicy.STATELESS  означает:
                        // Spring Security не будет создавать HTTP-сессии (например, HttpSession).
                        //Каждому запросу требуется токен аутентификации (в твоём случае — JWT от Keycloak).
                ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //Говорит Spring Security, что это приложение является ресурс-сервером OAuth2, который будет принимать JWT-токены (в данном случае от Keycloak)
                .oauth2ResourceServer(
                        //.jwtAuthenticationConverter(...) настраивает специальный конвертер, чтобы преобразовать поля JWT в GrantedAuthority
//                        OAuth2ResourceServerConfigurer::jwt)
                        resourceServer -> resourceServer.jwt(
                                jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(
                                        keycloakAuthConverter()//кастомный метод, возвращающий JwtAuthenticationConverter, где ты настраиваешь, какие роли брать из поля realm_access.roles, и как превращать их в ROLE_... для Spring.
                                )
                        )
                ).csrf(AbstractHttpConfigurer::disable);//Обычно CSRF защита нужна для форм на веб-страницах (сессии + куки
        //для REST API на токенах (stateless) — CSRF не нужен, поэтому мы его отключаем

        return http.build();
    }

    private Converter<Jwt, ? extends AbstractAuthenticationToken> keycloakAuthConverter() {
        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new AuthorityConverter());
        return converter;
    }

}