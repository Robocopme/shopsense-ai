package ai.shopsense.product.service;

import ai.shopsense.product.domain.ProductRecord;
import ai.shopsense.product.dto.CreateProductRequest;
import ai.shopsense.product.dto.ProductDto;
import ai.shopsense.product.dto.UpdateProductPriceRequest;
import ai.shopsense.product.exception.ProductNotFoundException;
import ai.shopsense.product.mapper.ProductMapper;
import ai.shopsense.product.repository.ProductRepository;
import ai.shopsense.shared.events.PriceEvent;
import ai.shopsense.shared.events.ProductEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public ProductDto create(CreateProductRequest request) {
        ProductRecord record = mapper.toEntity(request);
        repository.save(record);
        kafkaTemplate.send("product-events", new ProductEvent(record.getId(), record.getRetailer(), record.getCurrentPrice(), OffsetDateTime.now()));
        return mapper.toDto(record);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "products", key = "#id")
    public ProductDto findById(UUID id) {
        return mapper.toDto(repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product %s not found".formatted(id))));
    }

    @Transactional
    @CacheEvict(cacheNames = "products", key = "#id")
    @Retryable
    public ProductDto updatePrice(UUID id, UpdateProductPriceRequest request) {
        ProductRecord record = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product %s not found".formatted(id)));
        BigDecimal previous = record.getCurrentPrice();
        record.setCurrentPrice(request.getNewPrice());
        if (request.getCurrency() != null) {
            record.setCurrency(request.getCurrency());
        }
        record.setUpdatedAt(OffsetDateTime.now());
        repository.save(record);
                kafkaTemplate.send("product-events", new ProductEvent(record.getId(), record.getRetailer(), record.getCurrentPrice(), OffsetDateTime.now()));
                if (previous.subtract(record.getCurrentPrice()).abs().doubleValue() >= 1.0) {
                    kafkaTemplate.send("price-events", new PriceEvent(record.getId(), previous, record.getCurrentPrice(), OffsetDateTime.now()));
                }
        return mapper.toDto(record);
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> search(String retailer, String query, int page, int size) {
        return repository.findByRetailerIgnoreCaseAndTitleContainingIgnoreCase(retailer, query, PageRequest.of(page, size))
                .map(mapper::toDto);
    }
}
