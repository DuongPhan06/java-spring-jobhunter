package vn.hoidanit.jobhunter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import vn.hoidanit.jobhunter.service.UserService;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    private UserService userService;

    public SecurityConfiguration(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // String[] whiteList = {
        // "/",
        // "/api/v1/auth/login",
        // "/api/v1/auth/refresh",
        // "/api/v1/auth/register",
        // "/storage/**",
        // "/api/v1/companies/**",
        // "/api/v1/jobs/**",
        // "/api/v1/skills/**",
        // "/api/v1/files/**",
        // "/api/v1/email/**",
        // "/v3/api-docs/**",
        // "/swagger-ui/**",
        // "/swagger-ui.html"
        // };

        http
                .authorizeHttpRequests(
                        authz -> authz
                                .requestMatchers("/").permitAll()
                                .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

}
