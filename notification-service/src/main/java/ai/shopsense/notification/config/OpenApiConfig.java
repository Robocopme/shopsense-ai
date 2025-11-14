package ai.shopsense.notification.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI notificationserviceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Notification Service API")
                        .description("Email, push, and in-app fan-out with consent tracking.")
                        .version("v1")
                        .contact(new Contact().name("ShopSense AI").email("security@shopsense.ai"))
                        .license(new License().name("Commercial").url("https://shopsense.ai/terms"))
                );
    }
}
