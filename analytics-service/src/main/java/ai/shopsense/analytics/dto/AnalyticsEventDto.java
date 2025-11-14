package ai.shopsense.analytics.dto;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record AnalyticsEventDto(
        String eventType,
        String actor,
        String channel,
        String payload,
        OffsetDateTime occurredAt
) {
}
