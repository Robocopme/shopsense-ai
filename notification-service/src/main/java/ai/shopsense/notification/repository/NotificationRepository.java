package ai.shopsense.notification.repository;

import ai.shopsense.notification.domain.NotificationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<NotificationRequest, UUID> {
}
