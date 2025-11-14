package ai.shopsense.notification.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Configuration
public class TracingConfig {

    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> correlationFilter() {
        OncePerRequestFilter filter = new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                String correlationId = request.getHeader("X-Correlation-Id");
                if (correlationId == null || correlationId.isBlank()) {
                    correlationId = UUID.randomUUID().toString();
                }
                MDC.put("correlationId", correlationId);
                response.addHeader("X-Correlation-Id", correlationId);
                try {
                    filterChain.doFilter(request, response);
                } finally {
                    MDC.remove("correlationId");
                }
            }
        };
        FilterRegistrationBean<OncePerRequestFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }
}
