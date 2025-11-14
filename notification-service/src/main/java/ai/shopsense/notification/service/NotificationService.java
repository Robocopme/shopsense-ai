package ai.shopsense.notification.service;

import ai.shopsense.notification.domain.NotificationRequest;
import ai.shopsense.notification.dto.CreateNotificationRequest;
import ai.shopsense.notification.dto.NotificationDto;
import ai.shopsense.notification.exception.NotificationException;
import ai.shopsense.notification.mapper.NotificationMapper;
import ai.shopsense.notification.repository.NotificationRepository;
import ai.shopsense.shared.events.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;
    private final NotificationMapper mapper;
    private final JavaMailSender mailSender;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public NotificationDto send(CreateNotificationRequest request) {
        NotificationRequest entity = mapper.toEntity(request);
        repository.save(entity);
        dispatch(entity);
        kafkaTemplate.send("notification-events", new NotificationEvent(entity.getUserId(), entity.getProductId(), entity.getChannel(), entity.getPayload()));
        return mapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "notification-preferences", key = "#id")
    public NotificationDto findById(UUID id) {
        return mapper.toDto(repository.findById(id)
                .orElseThrow(() -> new NotificationException("Notification %s not found".formatted(id))));
    }

    private void dispatch(NotificationRequest request) {
        if ("EMAIL".equalsIgnoreCase(request.getChannel())) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(request.getDestination());
            message.setSubject("ShopSense Alert");
            message.setText(request.getPayload());
            mailSender.send(message);
        }
        // push/in-app could be integrated via FCM or webhooks
        request.setStatus("SENT");
        request.setSentAt(OffsetDateTime.now());
        repository.save(request);
    }
}
