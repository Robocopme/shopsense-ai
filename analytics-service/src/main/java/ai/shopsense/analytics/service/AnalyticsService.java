package ai.shopsense.analytics.service;

import ai.shopsense.analytics.domain.AnalyticsEvent;
import ai.shopsense.analytics.dto.AnalyticsEventDto;
import ai.shopsense.analytics.dto.CreateAnalyticsEventRequest;
import ai.shopsense.analytics.mapper.AnalyticsMapper;
import ai.shopsense.analytics.repository.AnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final AnalyticsRepository repository;
    private final AnalyticsMapper mapper;

    @Transactional
    public AnalyticsEventDto record(CreateAnalyticsEventRequest request) {
        AnalyticsEvent event = mapper.toEntity(request);
        repository.save(event);
        return mapper.toDto(event);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "analytics-views", key = "#from.toEpochSecond().toString().concat('-').concat(#to.toEpochSecond().toString())")
    public List<AnalyticsEventDto> between(OffsetDateTime from, OffsetDateTime to) {
        return repository.findByOccurredAtBetween(from, to).stream()
                .map(mapper::toDto)
                .toList();
    }
}
