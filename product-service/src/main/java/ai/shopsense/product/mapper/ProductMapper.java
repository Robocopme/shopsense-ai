package ai.shopsense.product.mapper;

import ai.shopsense.product.domain.ProductRecord;
import ai.shopsense.product.dto.CreateProductRequest;
import ai.shopsense.product.dto.ProductDto;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class ProductMapper {

    public ProductRecord toEntity(CreateProductRequest request) {
        return ProductRecord.builder()
                .id(UUID.randomUUID())
                .retailer(request.getRetailer())
                .sku(request.getSku())
                .title(request.getTitle())
                .url(request.getUrl())
                .imageUrl(request.getImageUrl())
                .currency(request.getCurrency())
                .currentPrice(request.getPrice())
                .status("ACTIVE")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }

    public ProductDto toDto(ProductRecord record) {
        return ProductDto.builder()
                .id(record.getId())
                .retailer(record.getRetailer())
                .sku(record.getSku())
                .title(record.getTitle())
                .url(record.getUrl())
                .imageUrl(record.getImageUrl())
                .currency(record.getCurrency())
                .currentPrice(record.getCurrentPrice())
                .status(record.getStatus())
                .updatedAt(record.getUpdatedAt())
                .build();
    }
}
