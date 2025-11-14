package ai.shopsense.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class UpdateUserRequest {
    @NotBlank
    private String fullName;

    private Boolean marketingOptIn;

    private Set<String> preferences;
}
