package ai.shopsense.shared.events;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ProductEvent(
        UUID productId,
        String retailer,
        BigDecimal price,
        OffsetDateTime occurredAt
) {
}
