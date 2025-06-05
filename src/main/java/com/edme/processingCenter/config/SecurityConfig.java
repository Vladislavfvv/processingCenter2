package com.edme.processingCenter.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity //поддержка аннотаций безопасности
public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/admin/**").hasRole("ADMIN")  // проверяет наличие ROLE_ADMIN
//                        .requestMatchers("/user/**").hasRole("USER")    // проверяет наличие ROLE_USER
//                        .anyRequest().authenticated()
//                )
//                .formLogin()
//                .and()
//                .logout();
//
//        return http.build();
//    }


//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/user/**").hasRole("USER")
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .successHandler(successHandler)
////                .formLogin(Customizer.withDefaults()) переход на /
//                .logout(Customizer.withDefaults())    // теперь так
//                .csrf(csrf -> csrf.disable()));        // теперь так
//
//        return http.build();
//    }

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/register", "/login", "/css/**", "/js/**").permitAll()
                    .requestMatchers("/public/**").permitAll()      // Публичные маршруты
                    .requestMatchers("/admin/**").hasRole("ADMIN")  //Только для ADMIN
                    .requestMatchers("/user/**").hasRole("USER")    //USER и ADMIN
                    .anyRequest().authenticated()                   //  Остальное требует авторизации
            )
            .formLogin(form -> form
                    .loginPage("/login")                   // указать страницу логина явно

                    .permitAll()                            // разрешить всем доступ к форме логина
            )
            .httpBasic(Customizer.withDefaults()) // <-- разрешаем  basic auth
            .logout(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable()); // для REST можно отключить CSRF

    return http.build();
}

    @Bean
    public PasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
        // return NoOpPasswordEncoder.getInstance(); // только для тестов
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
