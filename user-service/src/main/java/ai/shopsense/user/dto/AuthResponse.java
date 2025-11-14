package ai.shopsense.user.dto;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record AuthResponse(
        String accessToken,
        String refreshToken,
        OffsetDateTime expiresAt
) {
}
