package ai.shopsense.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @RequestMapping("/users")
    public ResponseEntity<String> userFallback() {
        return ResponseEntity.accepted().body("User service temporarily unavailable, serving cached data.");
    }

    @RequestMapping("/products")
    public ResponseEntity<String> productFallback() {
        return ResponseEntity.accepted().body("Product service degraded. Please retry.");
    }

    @RequestMapping("/prices")
    public ResponseEntity<String> priceFallback() {
        return ResponseEntity.accepted().body("Price service busy. Alerts queued.");
    }
}
