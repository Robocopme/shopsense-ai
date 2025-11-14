package ai.shopsense.shared.events;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record PriceEvent(
        UUID productId,
        BigDecimal previousPrice,
        BigDecimal newPrice,
        OffsetDateTime detectedAt
) {
}
