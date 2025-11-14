package ai.shopsense.price.mapper;

import ai.shopsense.price.domain.PriceWatch;
import ai.shopsense.price.dto.CreatePriceWatchRequest;
import ai.shopsense.price.dto.PriceWatchDto;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class PriceMapper {

    public PriceWatch toEntity(CreatePriceWatchRequest request) {
        return PriceWatch.builder()
                .id(UUID.randomUUID())
                .productId(request.getProductId())
                .lastPrice(request.getLastPrice())
                .targetPrice(request.getTargetPrice())
                .nextCheckAt(OffsetDateTime.now().plusHours(1))
                .active(true)
                .status("TRACKING")
                .createdAt(OffsetDateTime.now())
                .build();
    }

    public PriceWatchDto toDto(PriceWatch watch) {
        return PriceWatchDto.builder()
                .id(watch.getId())
                .productId(watch.getProductId())
                .lastPrice(watch.getLastPrice())
                .targetPrice(watch.getTargetPrice())
                .nextCheckAt(watch.getNextCheckAt())
                .active(watch.getActive())
                .status(watch.getStatus())
                .build();
    }
}
