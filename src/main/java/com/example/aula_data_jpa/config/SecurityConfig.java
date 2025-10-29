package com.example.aula_data_jpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        DefaultSecurityFilterChain buildHttpSecurity =
                httpSecurity.csrf(httpCsrf -> httpCsrf.disable())
                        .authorizeHttpRequests(authorize -> {
                                    authorize.requestMatchers(HttpMethod.POST, "/usuario/add").permitAll()
                                            .requestMatchers("/usuario/buscar").hasAnyRole("ADMIN")
                                    .anyRequest().authenticated();
                                }
                                )
                        .httpBasic(Customizer.withDefaults()).build();
        return buildHttpSecurity;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
