package ai.shopsense.product.domain;

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
@Table(name = "products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRecord {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String retailer;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String url;

    private String imageUrl;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private BigDecimal currentPrice;

    @Column(nullable = false)
    private String status;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}
