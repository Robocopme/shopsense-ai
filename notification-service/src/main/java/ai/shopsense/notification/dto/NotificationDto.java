package ai.shopsense.notification.dto;

import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
public record NotificationDto(
        UUID id,
        UUID userId,
        UUID productId,
        String channel,
        String destination,
        String status,
        OffsetDateTime sentAt
) {
}
