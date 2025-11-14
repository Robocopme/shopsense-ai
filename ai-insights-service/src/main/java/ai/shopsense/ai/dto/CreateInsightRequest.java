package ai.shopsense.ai.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateInsightRequest {
    @NotNull
    private UUID productId;

    private String question;

    private Boolean forceRefresh = Boolean.FALSE;
}
