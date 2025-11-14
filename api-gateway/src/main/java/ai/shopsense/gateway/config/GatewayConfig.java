package ai.shopsense.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/api/v1/users/**")
                        .filters(f -> f.requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("X-User-Id"))
                                                .switchIfEmpty(Mono.justOrEmpty(exchange.getRequest().getRemoteAddress()).map(addr -> addr.getHostString())))))
                        .uri("http://user-service:8081"))
                .route("product-service", r -> r.path("/api/v1/products/**")
                        .filters(f -> f.circuitBreaker(c -> c.setName("productService").setFallbackUri("forward:/fallback/products")))
                        .uri("http://product-service:8082"))
                .route("price-service", r -> r.path("/api/v1/prices/**")
                        .filters(f -> f.circuitBreaker(c -> c.setName("priceService").setFallbackUri("forward:/fallback/prices")))
                        .uri("http://price-service:8083"))
                .route("review-service", r -> r.path("/api/v1/reviews/**")
                        .uri("http://review-service:8084"))
                .route("ai-insights-service", r -> r.path("/api/v1/insights/**")
                        .filters(f -> f.requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("Authorization")))))
                        .uri("http://ai-insights-service:8085"))
                .route("notification-service", r -> r.path("/api/v1/notifications/**")
                        .uri("http://notification-service:8086"))
                .route("analytics-service", r -> r.path("/api/v1/analytics/**")
                        .uri("http://analytics-service:8087"))
                .build();
    }

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(100, 200);
    }
}
