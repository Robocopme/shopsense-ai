package ai.shopsense.product.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateProductPriceRequest {
    @NotNull
    private BigDecimal newPrice;

    private String currency;
}
