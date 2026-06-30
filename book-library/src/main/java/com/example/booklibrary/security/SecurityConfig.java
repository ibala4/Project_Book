package com.example.booklibrary.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.http.HttpStatus;

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
                // Public: the login/register pages themselves, their API endpoints,
                // and static assets (css/js/fonts) needed to render those pages.
                .requestMatchers(
                    "/login.html", "/register.html",
                    "/api/auth/register", "/api/auth/logout",
                    "/css/**", "/js/**", "/images/**",
                    "/swagger-ui/**", "/v3/api-docs/**"
                ).permitAll()
                // Everything else (the Shelf UI and the book API) requires login.
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
                .logoutRequestMatcher(
                    new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/auth/logout", "POST")
                )
                .logoutSuccessUrl("/login.html")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            // CSRF is disabled here to keep the plain HTML/JS frontend simple
            // (no CSRF token plumbing needed in fetch() calls). This is a
            // reasonable trade-off for a learning project; a production app
            // would keep CSRF protection enabled and pass the token through.
            .headers(headers -> headers
                .cacheControl(cache -> {}) // sends Cache-Control: no-cache, no-store on every response
            )
            .csrf(AbstractHttpConfigurer::disable)
            // For unauthenticated API calls (fetch() from JS), return a plain
            // 401 instead of redirecting to the login page — redirecting an
            // AJAX call just gives the JS a confusing HTML page instead of JSON.
            .exceptionHandling(ex -> ex
                .defaultAuthenticationEntryPointFor(
                    new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                    new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/**")
                )
            );

        return http.build();
    }
}
