package ai.shopsense.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductRequest {
    @NotBlank
    private String retailer;

    @NotBlank
    private String sku;

    @NotBlank
    @Size(min = 4, max = 180)
    private String title;

    @NotBlank
    private String url;

    private String imageUrl;

    @NotBlank
    private String currency;

    @NotNull
    private BigDecimal price;
}
