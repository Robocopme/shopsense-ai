package ai.shopsense.product.controller;

import ai.shopsense.product.dto.CreateProductRequest;
import ai.shopsense.product.dto.ProductDto;
import ai.shopsense.product.dto.UpdateProductPriceRequest;
import ai.shopsense.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> create(@Valid @RequestBody CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PutMapping("/{id}/price")
    public ResponseEntity<ProductDto> updatePrice(@PathVariable UUID id, @Valid @RequestBody UpdateProductPriceRequest request) {
        return ResponseEntity.ok(productService.updatePrice(id, request));
    }

    @GetMapping
    public Page<ProductDto> search(@RequestParam String retailer,
                                    @RequestParam(defaultValue = "") String q,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "20") int size) {
        return productService.search(retailer, q, page, size);
    }
}
