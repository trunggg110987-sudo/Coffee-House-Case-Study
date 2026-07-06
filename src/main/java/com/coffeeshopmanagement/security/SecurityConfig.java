package com.coffeeshopmanagement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/login",
//                                "/css/**",
//                                "/js/**",
//                                "/images/**",
//                                "/webjars/**"
//                        ).permitAll()

//                        .requestMatchers(
//                                "/categories/**",
//                                "/products/**",
//                                "/tables/**",
//                                "/ingredients/**",
//                                "/recipes/**"
//                        ).hasRole("ADMIN")

//                        .requestMatchers(
//                                "/orders/**",
//                                "/payments/**",
//                                "/dashboard/**"
//                        ).hasAnyRole("ADMIN", "STAFF")

//                        .anyRequest().authenticated()
//                )
                        .authorizeHttpRequests(auth -> auth
                                .anyRequest().permitAll()
                        )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // 401 Unauthorized
                        .accessDeniedHandler(new CustomAccessDeniedHandler())           // 403 Forbidden
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}