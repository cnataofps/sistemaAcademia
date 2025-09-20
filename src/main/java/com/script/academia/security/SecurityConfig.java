package com.script.academia.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/cadastrarUsuario").permitAll()
                .requestMatchers("/").authenticated()

                // Dashboards personalizados
                .requestMatchers("/dashboard/admin/**").hasRole("ADMIN")
                .requestMatchers("/dashboard/professor/**").hasRole("PROFESSOR")
                .requestMatchers("/dashboard/aluno/**").hasRole("ALUNO")

                // Acesso geral por perfil
                .requestMatchers("/configuracoes", "/aluno/**").hasAnyRole("ADMIN", "PROFESSOR", "ALUNO")
                .requestMatchers("/cadastrarAlunos", "/ficha/**", "/avaliacao/**").hasAnyRole("ADMIN", "PROFESSOR")
                .requestMatchers("/admin/**", "/suporte/receber").hasRole("ADMIN")
                .requestMatchers("/suporte/enviar").hasAnyRole("ADMIN", "PROFESSOR")
                .requestMatchers("/aluno/minhaFicha", "/aluno/minhaAvaliacao").hasRole("ALUNO")

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}