package com.uti.classbookingservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Socio
                        .requestMatchers(HttpMethod.POST, "/api/bookings").hasRole("MEMBER")
                        .requestMatchers("/api/bookings/my-bookings").hasRole("MEMBER")

                        // Entrenador
                        .requestMatchers("/api/bookings/my-classes").hasRole("TRAINER")
                        .requestMatchers("/api/bookings/*/attend").hasRole("TRAINER")

                        // Admin
                        .requestMatchers(HttpMethod.GET, "/api/bookings").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/bookings/**").hasRole("ADMIN")

                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConvert()))
                );
        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConvert() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(this::extractRealmRole);
        return converter;
    }


    @SuppressWarnings("unchecked")
    private Collection<GrantedAuthority> extractRealmRole(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null || realmAccess.get("roles") == null) {
            return List.of();
        }

        List<String> roles = (List<String>) realmAccess.get("roles");

        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }
}
