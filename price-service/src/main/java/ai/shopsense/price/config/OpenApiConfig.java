package ai.shopsense.price.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI priceserviceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Price Tracking Service API")
                        .description("Historical price storage and deal detection.")
                        .version("v1")
                        .contact(new Contact().name("ShopSense AI").email("security@shopsense.ai"))
                        .license(new License().name("Commercial").url("https://shopsense.ai/terms"))
                );
    }
}
