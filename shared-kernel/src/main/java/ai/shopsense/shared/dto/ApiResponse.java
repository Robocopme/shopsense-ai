package ai.shopsense.shared.dto;

import lombok.Builder;

@Builder
public record ApiResponse<T>(boolean success, String message, T data) {
}
