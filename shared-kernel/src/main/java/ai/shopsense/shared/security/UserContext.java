package ai.shopsense.shared.security;

import java.util.UUID;

public record UserContext(UUID userId, String role) {
}
