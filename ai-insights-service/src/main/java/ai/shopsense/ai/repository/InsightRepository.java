package ai.shopsense.ai.repository;

import ai.shopsense.ai.domain.InsightTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InsightRepository extends JpaRepository<InsightTask, UUID> {
    Optional<InsightTask> findTopByProductIdOrderByCompletedAtDesc(UUID productId);
}
