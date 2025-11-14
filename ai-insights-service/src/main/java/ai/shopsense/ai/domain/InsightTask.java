package ai.shopsense.ai.domain;

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
@Table(name = "insight_tasks")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsightTask {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID productId;

    @Lob
    private String prompt;

    @Lob
    private String response;

    private String status;

    private OffsetDateTime requestedAt;

    private OffsetDateTime completedAt;

    private String recommendation;
}
