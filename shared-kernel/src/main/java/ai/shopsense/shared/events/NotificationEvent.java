package ai.shopsense.shared.events;

import java.util.UUID;

public record NotificationEvent(
        UUID userId,
        UUID productId,
        String channel,
        String payload
) {
}
