package ai.shopsense.analytics.repository;

import ai.shopsense.analytics.domain.AnalyticsEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface AnalyticsRepository extends JpaRepository<AnalyticsEvent, UUID> {
    List<AnalyticsEvent> findByOccurredAtBetween(OffsetDateTime from, OffsetDateTime to);
}
