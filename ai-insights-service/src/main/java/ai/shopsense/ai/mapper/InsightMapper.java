package ai.shopsense.ai.mapper;

import ai.shopsense.ai.domain.InsightTask;
import ai.shopsense.ai.dto.InsightDto;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class InsightMapper {

    public InsightDto toDto(InsightTask task) {
        List<String> sections = task.getResponse() == null ? List.of() : Arrays.stream(task.getResponse().split("\n\n"))
                .map(String::trim)
                .filter(line -> !line.isBlank())
                .toList();
        return InsightDto.builder()
                .id(task.getId())
                .productId(task.getProductId())
                .recommendation(task.getRecommendation())
                .sections(sections)
                .generatedAt(task.getCompletedAt() == null ? OffsetDateTime.now() : task.getCompletedAt())
                .build();
    }
}
