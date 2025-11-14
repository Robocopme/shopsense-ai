package ai.shopsense.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateReviewAnalysisRequest {
    @NotNull
    private UUID productId;

    @NotEmpty
    private List<@NotBlank String> reviews;
}
