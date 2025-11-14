package ai.shopsense.analytics.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateAnalyticsEventRequest {
    @NotBlank
    private String eventType;

    @NotBlank
    private String actor;

    @NotBlank
    private String channel;

    private String payload;
}
