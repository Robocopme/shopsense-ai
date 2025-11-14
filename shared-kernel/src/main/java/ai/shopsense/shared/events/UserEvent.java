package ai.shopsense.shared.events;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserEvent(
        UUID userId,
        String action,
        OffsetDateTime occurredAt
) {
}
