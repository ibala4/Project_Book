package com.example.dmart.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * BCrypt hashes passwords with a built-in random salt, so the same
     * password produces a different hash every time it's stored, and the
     * hash can't practically be reversed back to the original password.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/login.html", "/register.html",
                    "/api/auth/register",
                    "/css/**", "/js/**", "/images/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login.html")
                .loginProcessingUrl("/api/auth/login")
                .defaultSuccessUrl("/index.html", true)
                .failureUrl("/login.html?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/api/auth/logout", "POST"))
                .logoutSuccessUrl("/login.html")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            // CSRF disabled to keep the plain HTML/JS frontend's fetch() calls
            // simple — a reasonable trade-off for a learning project.
            .csrf(AbstractHttpConfigurer::disable)
            // Static HTML/CSS/JS responses must never be cached by the browser
            // — login state can change between requests, and a cached page
            // would show stale content after logout. Set from the start here,
            // both at the Security-header level AND in application.properties
            // (Spring Boot's static-resource handler has its own separate
            // caching system that can otherwise conflict with this).
            .headers(headers -> headers
                .cacheControl(cache -> {})
            )
            // For unauthenticated AJAX calls (fetch() from JS), return a plain
            // 401 instead of redirecting to the login page — redirecting an
            // AJAX call just gives the JS a confusing HTML page instead of JSON.
            .exceptionHandling(ex -> ex
                .defaultAuthenticationEntryPointFor(
                    new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                    new AntPathRequestMatcher("/api/**")
                )
            );

        return http.build();
    }
}
