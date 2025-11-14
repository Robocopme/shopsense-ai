package ai.shopsense.analytics.mapper;

import ai.shopsense.analytics.domain.AnalyticsEvent;
import ai.shopsense.analytics.dto.AnalyticsEventDto;
import ai.shopsense.analytics.dto.CreateAnalyticsEventRequest;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class AnalyticsMapper {

    public AnalyticsEvent toEntity(CreateAnalyticsEventRequest request) {
        return AnalyticsEvent.builder()
                .id(UUID.randomUUID())
                .eventType(request.getEventType())
                .actor(request.getActor())
                .channel(request.getChannel())
                .payload(request.getPayload())
                .occurredAt(OffsetDateTime.now())
                .build();
    }

    public AnalyticsEventDto toDto(AnalyticsEvent event) {
        return AnalyticsEventDto.builder()
                .eventType(event.getEventType())
                .actor(event.getActor())
                .channel(event.getChannel())
                .payload(event.getPayload())
                .occurredAt(event.getOccurredAt())
                .build();
    }
}
