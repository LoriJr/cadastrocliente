package com.viratech.cadastrocliente.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class ConfigSecurity {

    @Value("${app.cors.allowed-origin}")
    private String allowedOrigin;

    private final FilterAccessToken filterAccessToken;

    public ConfigSecurity(FilterAccessToken filterAccessToken) {
        this.filterAccessToken = filterAccessToken;
    }

    @Bean
    public SecurityFilterChain filterSecurity(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors -> {})
                .authorizeHttpRequests(
                        req -> {
                            req.requestMatchers("/api/v1/auth/login", "/api/v1/auth/refresh-token").permitAll();
                            req.requestMatchers(HttpMethod.POST, "/users", "/api/v1/auth/register").permitAll();
                            req.requestMatchers(HttpMethod.GET, "/api/v1/auth/verify").permitAll();

                            req.requestMatchers(HttpMethod.GET, "/users/**").hasRole("ADMIN");
                            req.requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN");
                            req.requestMatchers(HttpMethod.PATCH, "/add-role/**").hasRole("ADMIN");


                            req.anyRequest().authenticated();
                        }
                )
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(filterAccessToken, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder encripter(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(allowedOrigin));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public RoleHierarchy roleHierarchy(){
        String hierarchy = "ROLE_ADMIN > ROLE_USER";
        return RoleHierarchyImpl.fromHierarchy(hierarchy);
    }
}
