package ai.shopsense.user.mapper;

import ai.shopsense.user.domain.UserAccount;
import ai.shopsense.user.dto.CreateUserRequest;
import ai.shopsense.user.dto.UpdateUserRequest;
import ai.shopsense.user.dto.UserDto;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class UserMapper {

    public UserAccount toEntity(CreateUserRequest request) {
        return UserAccount.builder()
                .id(UUID.randomUUID())
                .email(request.getEmail().toLowerCase())
                .fullName(request.getFullName())
                .passwordHash(request.getPassword())
                .role(request.getRole())
                .marketingOptIn(request.getMarketingOptIn())
                .preferences(request.getPreferences())
                .consentVersion("v1.0")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }

    public void update(UserAccount account, UpdateUserRequest request) {
        account.setFullName(request.getFullName());
        if (request.getMarketingOptIn() != null) {
            account.setMarketingOptIn(request.getMarketingOptIn());
        }
        if (request.getPreferences() != null) {
            account.setPreferences(request.getPreferences());
        }
        account.setUpdatedAt(OffsetDateTime.now());
    }

    public UserDto toDto(UserAccount account) {
        return UserDto.builder()
                .id(account.getId())
                .email(account.getEmail())
                .fullName(account.getFullName())
                .role(account.getRole())
                .marketingOptIn(account.getMarketingOptIn())
                .preferences(account.getPreferences())
                .createdAt(account.getCreatedAt())
                .build();
    }
}
