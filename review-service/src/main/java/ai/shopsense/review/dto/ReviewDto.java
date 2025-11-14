package ai.shopsense.review.dto;

import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
public record ReviewDto(
        UUID id,
        UUID productId,
        Double sentimentScore,
        Double authenticityScore,
        Integer reviewCount,
        String summary,
        OffsetDateTime generatedAt
) {
}
