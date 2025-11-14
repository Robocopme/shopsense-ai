package ai.shopsense.ai;

import ai.shopsense.shared.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class AiInsightsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiInsightsServiceApplication.class, args);
    }
}
