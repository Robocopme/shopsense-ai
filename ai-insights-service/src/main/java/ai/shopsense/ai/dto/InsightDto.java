package ai.shopsense.ai.dto;

import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record InsightDto(
        UUID id,
        UUID productId,
        String recommendation,
        List<String> sections,
        OffsetDateTime generatedAt
) {
}
