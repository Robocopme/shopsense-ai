package ai.shopsense.review.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "review_aggregates")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewAggregate {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private Double sentimentScore;

    @Column(nullable = false)
    private Double authenticityScore;

    @Column(nullable = false)
    private Integer reviewCount;

    @Lob
    private String summary;

    @Column(nullable = false)
    private OffsetDateTime generatedAt;
}
