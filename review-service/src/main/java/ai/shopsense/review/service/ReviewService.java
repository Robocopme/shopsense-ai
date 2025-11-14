package ai.shopsense.review.service;

import ai.shopsense.review.domain.ReviewAggregate;
import ai.shopsense.review.dto.CreateReviewAnalysisRequest;
import ai.shopsense.review.dto.ReviewDto;
import ai.shopsense.review.exception.ReviewNotFoundException;
import ai.shopsense.review.mapper.ReviewMapper;
import ai.shopsense.review.repository.ReviewRepository;
import ai.shopsense.shared.events.ProductEvent;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository repository;
    private final ReviewMapper mapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public ReviewDto analyze(CreateReviewAnalysisRequest request) {
        double sentiment = request.getReviews().stream()
                .mapToInt(review -> StringUtils.countMatches(review.toLowerCase(), "good") - StringUtils.countMatches(review.toLowerCase(), "bad"))
                .average()
                .orElse(0.0);
        double authenticity = 1.0 - Math.min(1.0, request.getReviews().size() / 100.0);
        ReviewAggregate aggregate = ReviewAggregate.builder()
                .id(UUID.randomUUID())
                .productId(request.getProductId())
                .sentimentScore(Math.tanh(sentiment))
                .authenticityScore(authenticity)
                .reviewCount(request.getReviews().size())
                .summary("Aggregated %d fresh reviews".formatted(request.getReviews().size()))
                .generatedAt(OffsetDateTime.now())
                .build();
        repository.save(aggregate);
        kafkaTemplate.send("review-events", new ProductEvent(aggregate.getProductId(), "review", BigDecimal.ZERO, OffsetDateTime.now()));
        return mapper.toDto(aggregate);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "review-aggregates", key = "#productId")
    public ReviewDto latest(UUID productId) {
        return repository.findTopByProductIdOrderByGeneratedAtDesc(productId)
                .map(mapper::toDto)
                .orElseThrow(() -> new ReviewNotFoundException("No reviews for product %s".formatted(productId)));
    }
}
