package ai.shopsense.price.repository;

import ai.shopsense.price.domain.PriceWatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface PriceWatchRepository extends JpaRepository<PriceWatch, UUID> {
    List<PriceWatch> findByActiveTrueAndNextCheckAtBefore(OffsetDateTime time);
}
