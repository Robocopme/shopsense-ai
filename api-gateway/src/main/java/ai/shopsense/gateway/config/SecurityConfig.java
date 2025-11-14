package ai.shopsense.gateway.config;

import ai.shopsense.gateway.security.GatewayJwtAuthenticationManager;
import ai.shopsense.shared.security.JwtProperties;
import ai.shopsense.shared.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public JwtTokenProvider gatewayJwtTokenProvider(JwtProperties properties) {
        return new JwtTokenProvider(properties);
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, AuthenticationWebFilter jwtWebFilter) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/actuator/**",
                                "/swagger",
                                "/swagger/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/webjars/**",
                                "/v3/api-docs/**",
                                "/fallback/**").permitAll()
                        .pathMatchers("/api/v1/users/auth/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                        .anyExchange().authenticated())
                .addFilterAt(jwtWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public AuthenticationWebFilter jwtWebFilter(GatewayJwtAuthenticationManager authenticationManager) {
        AuthenticationWebFilter filter = new AuthenticationWebFilter(authenticationManager);
        filter.setRequiresAuthenticationMatcher(exchange -> {
            String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (header != null && header.startsWith("Bearer ")) {
                return ServerWebExchangeMatcher.MatchResult.match();
            }
            return ServerWebExchangeMatcher.MatchResult.notMatch();
        });
        filter.setServerAuthenticationConverter(exchange -> {
            String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);
                return Mono.just(new UsernamePasswordAuthenticationToken(token, token));
            }
            return Mono.empty();
        });
        return filter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Request-Id"));
        configuration.setAllowCredentials(false);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
