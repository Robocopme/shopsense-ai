package ai.shopsense.price.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "price_watches")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceWatch {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private BigDecimal lastPrice;

    @Column(nullable = false)
    private BigDecimal targetPrice;

    @Column(nullable = false)
    private OffsetDateTime nextCheckAt;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private OffsetDateTime createdAt;
}
