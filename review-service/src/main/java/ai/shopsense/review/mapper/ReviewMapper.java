package ai.shopsense.review.mapper;

import ai.shopsense.review.domain.ReviewAggregate;
import ai.shopsense.review.dto.ReviewDto;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewDto toDto(ReviewAggregate aggregate) {
        return ReviewDto.builder()
                .id(aggregate.getId())
                .productId(aggregate.getProductId())
                .sentimentScore(aggregate.getSentimentScore())
                .authenticityScore(aggregate.getAuthenticityScore())
                .reviewCount(aggregate.getReviewCount())
                .summary(aggregate.getSummary())
                .generatedAt(aggregate.getGeneratedAt())
                .build();
    }
}
