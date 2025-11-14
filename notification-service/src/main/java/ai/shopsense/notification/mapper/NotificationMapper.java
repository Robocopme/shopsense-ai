package ai.shopsense.notification.mapper;

import ai.shopsense.notification.domain.NotificationRequest;
import ai.shopsense.notification.dto.CreateNotificationRequest;
import ai.shopsense.notification.dto.NotificationDto;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class NotificationMapper {

    public NotificationRequest toEntity(CreateNotificationRequest request) {
        return NotificationRequest.builder()
                .id(UUID.randomUUID())
                .userId(request.getUserId())
                .productId(request.getProductId())
                .channel(request.getChannel())
                .destination(request.getDestination())
                .payload(request.getMessage())
                .status("QUEUED")
                .createdAt(OffsetDateTime.now())
                .build();
    }

    public NotificationDto toDto(NotificationRequest entity) {
        return NotificationDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .productId(entity.getProductId())
                .channel(entity.getChannel())
                .destination(entity.getDestination())
                .status(entity.getStatus())
                .sentAt(entity.getSentAt())
                .build();
    }
}
