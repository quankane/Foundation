package com.group2.restaurantorderingwebapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(AuthenticationProvider authenticationProvider, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer -> corsFilter())
                .authorizeHttpRequests(config -> config
                        // Public Endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers(
                                "swagger-ui/index.html/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categories/**", "/api/dishes/**", "/api/rankings/**", "/api/positions/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/orders").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/orders/{orderId}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/orders/{orderId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/orders/{orderId}", "/api/orders/position/{positionId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/order/{orderId}/update-user").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/{userId}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/payments/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/payments/{paymentId}", "/api/payments/bill").permitAll()
                        // User Role Endpoints
                        .requestMatchers(HttpMethod.POST, "api/reservations/create-reservation").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/reservations/{userId}").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/orders/user/{userId}").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/rankings").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "api/carts/**").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "api/carts/**").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/orders/cart/{cartId}").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/users/{userId}").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/payments/user/{userId}").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/favorites/**").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/favorites/**").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/favorites/**").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "api/reservations/{reservationId}").hasRole("USER")
                        // Admin and Manager Endpoints
                        .requestMatchers(HttpMethod.PATCH, "api/reservations/{reservationId}/update-status").hasRole("MANAGER")
                        // Catch-all rule: All other requests require ADMIN or MANAGER role
                        .anyRequest().hasAnyRole("ADMIN", "MANAGER")
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}