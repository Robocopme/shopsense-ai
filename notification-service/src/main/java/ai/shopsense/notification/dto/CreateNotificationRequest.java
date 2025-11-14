package ai.shopsense.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateNotificationRequest {
    @NotNull
    private UUID userId;

    @NotNull
    private UUID productId;

    @NotBlank
    private String channel;

    @NotBlank
    private String destination;

    @NotBlank
    private String message;
}
