package ai.shopsense.review.repository;

import ai.shopsense.review.domain.ReviewAggregate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<ReviewAggregate, UUID> {
    Optional<ReviewAggregate> findTopByProductIdOrderByGeneratedAtDesc(UUID productId);
}
