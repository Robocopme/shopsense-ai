package ai.shopsense.ai.service;

import ai.shopsense.ai.client.OllamaClient;
import ai.shopsense.ai.domain.InsightTask;
import ai.shopsense.ai.dto.CreateInsightRequest;
import ai.shopsense.ai.dto.InsightDto;
import ai.shopsense.ai.exception.InsightNotFoundException;
import ai.shopsense.ai.mapper.InsightMapper;
import ai.shopsense.ai.repository.InsightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InsightService {

    private final InsightRepository repository;
    private final InsightMapper mapper;
    private final OllamaClient ollamaClient;

    @Transactional
    @CacheEvict(cacheNames = "insights", key = "#request.productId")
    public InsightDto generate(CreateInsightRequest request) {
        if (!Boolean.TRUE.equals(request.getForceRefresh())) {
            return repository.findTopByProductIdOrderByCompletedAtDesc(request.getProductId())
                    .map(mapper::toDto)
                    .orElseGet(() -> createInsight(request));
        }
        return createInsight(request);
    }

    private InsightDto createInsight(CreateInsightRequest request) {
        InsightTask task = InsightTask.builder()
                .id(UUID.randomUUID())
                .productId(request.getProductId())
                .prompt(buildPrompt(request))
                .status("PENDING")
                .requestedAt(OffsetDateTime.now())
                .build();
        repository.save(task);
        String response = ollamaClient.generate(task.getPrompt());
        task.setResponse(response);
        task.setRecommendation(response.lines().findFirst().orElse("Review pending"));
        task.setStatus("COMPLETED");
        task.setCompletedAt(OffsetDateTime.now());
        repository.save(task);
        return mapper.toDto(task);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "insights", key = "#productId")
    public InsightDto latest(UUID productId) {
        return repository.findTopByProductIdOrderByCompletedAtDesc(productId)
                .map(mapper::toDto)
                .orElseThrow(() -> new InsightNotFoundException("No insight for product %s".formatted(productId)));
    }

    private String buildPrompt(CreateInsightRequest request) {
        String question = request.getQuestion() == null || request.getQuestion().isBlank()
                ? "What should a smart shopper know about this product?"
                : request.getQuestion();
        return "You are ShopSense AI. Provide recommendation, price outlook, quality risks, and ethical considerations. Question: " + question;
    }
}
