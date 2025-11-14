package ai.shopsense.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class CreateUserRequest {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String fullName;

    @Size(min = 12, message = "Use a strong password (12+ chars)")
    private String password;

    @NotBlank
    private String role;

    @NotNull
    private Boolean marketingOptIn;

    private Set<@Size(max = 50) String> preferences;
}
