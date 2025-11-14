package ai.shopsense.price.service;

import ai.shopsense.price.domain.PriceWatch;
import ai.shopsense.price.dto.CreatePriceWatchRequest;
import ai.shopsense.price.dto.PriceUpdateRequest;
import ai.shopsense.price.dto.PriceWatchDto;
import ai.shopsense.price.exception.PriceWatchNotFoundException;
import ai.shopsense.price.mapper.PriceMapper;
import ai.shopsense.price.repository.PriceWatchRepository;
import ai.shopsense.shared.events.PriceEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PriceService {

    private final PriceWatchRepository repository;
    private final PriceMapper mapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public PriceWatchDto createWatch(CreatePriceWatchRequest request) {
        PriceWatch watch = mapper.toEntity(request);
        repository.save(watch);
        return mapper.toDto(watch);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "price-watches", key = "#id")
    public PriceWatchDto findById(UUID id) {
        return mapper.toDto(repository.findById(id)
                .orElseThrow(() -> new PriceWatchNotFoundException("Price watch %s not found".formatted(id))));
    }

    @Transactional
    public PriceWatchDto recordPrice(UUID id, PriceUpdateRequest request) {
        PriceWatch watch = repository.findById(id)
                .orElseThrow(() -> new PriceWatchNotFoundException("Price watch %s not found".formatted(id)));
        BigDecimal previous = watch.getLastPrice();
        watch.setLastPrice(request.getObservedPrice());
        watch.setNextCheckAt(OffsetDateTime.now().plusHours(1));
        if (request.getObservedPrice().compareTo(watch.getTargetPrice()) <= 0) {
            watch.setStatus("THRESHOLD_MET");
            kafkaTemplate.send("price-events", new PriceEvent(watch.getProductId(), previous, request.getObservedPrice(), OffsetDateTime.now()));
        }
        repository.save(watch);
        return mapper.toDto(watch);
    }

    @Transactional(readOnly = true)
    public List<PriceWatchDto> dueForCheck() {
        return repository.findByActiveTrueAndNextCheckAtBefore(OffsetDateTime.now())
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Scheduled(cron = "0 0 * * * *")
    public void scheduleChecks() {
        repository.findByActiveTrueAndNextCheckAtBefore(OffsetDateTime.now()).forEach(watch -> {
            kafkaTemplate.send("price-events", new PriceEvent(watch.getProductId(), watch.getLastPrice(), watch.getLastPrice(), OffsetDateTime.now()));
        });
    }
}
