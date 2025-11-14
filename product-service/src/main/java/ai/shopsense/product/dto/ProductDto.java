package ai.shopsense.product.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
public record ProductDto(
        UUID id,
        String retailer,
        String sku,
        String title,
        String url,
        String imageUrl,
        String currency,
        BigDecimal currentPrice,
        String status,
        OffsetDateTime updatedAt
) {
}
