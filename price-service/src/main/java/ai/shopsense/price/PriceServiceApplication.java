package ai.shopsense.price;

import ai.shopsense.shared.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
@EnableScheduling
public class PriceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PriceServiceApplication.class, args);
    }
}
