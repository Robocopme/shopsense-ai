package ai.shopsense.user.dto;

import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
public record UserDto(
        UUID id,
        String email,
        String fullName,
        String role,
        Boolean marketingOptIn,
        Set<String> preferences,
        OffsetDateTime createdAt
) {
}
