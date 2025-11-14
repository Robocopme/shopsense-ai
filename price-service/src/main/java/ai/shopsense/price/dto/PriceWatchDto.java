package ai.shopsense.price.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
public record PriceWatchDto(
        UUID id,
        UUID productId,
        BigDecimal lastPrice,
        BigDecimal targetPrice,
        OffsetDateTime nextCheckAt,
        Boolean active,
        String status
) {
}
