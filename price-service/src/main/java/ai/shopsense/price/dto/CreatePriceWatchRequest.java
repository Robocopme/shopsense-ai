package ai.shopsense.price.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreatePriceWatchRequest {
    @NotNull
    private UUID productId;

    @NotNull
    private BigDecimal lastPrice;

    @NotNull
    private BigDecimal targetPrice;
}
