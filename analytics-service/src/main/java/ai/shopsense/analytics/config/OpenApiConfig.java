package ai.shopsense.analytics.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI analyticsserviceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Analytics Service API")
                        .description("Usage telemetry, KPI aggregation, and BI exports.")
                        .version("v1")
                        .contact(new Contact().name("ShopSense AI").email("security@shopsense.ai"))
                        .license(new License().name("Commercial").url("https://shopsense.ai/terms"))
                );
    }
}
