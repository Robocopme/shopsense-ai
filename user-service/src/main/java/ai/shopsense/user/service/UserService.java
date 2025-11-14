package ai.shopsense.user.service;

import ai.shopsense.shared.events.UserEvent;
import ai.shopsense.shared.security.JwtTokenProvider;
import ai.shopsense.user.domain.UserAccount;
import ai.shopsense.user.dto.AuthRequest;
import ai.shopsense.user.dto.AuthResponse;
import ai.shopsense.user.dto.CreateUserRequest;
import ai.shopsense.user.dto.UpdateUserRequest;
import ai.shopsense.user.dto.UserDto;
import ai.shopsense.user.exception.DuplicateUserException;
import ai.shopsense.user.exception.UserNotFoundException;
import ai.shopsense.user.mapper.UserMapper;
import ai.shopsense.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public UserDto register(CreateUserRequest request) {
        repository.findByEmailIgnoreCase(request.getEmail()).ifPresent(user -> {
            throw new DuplicateUserException(request.getEmail());
        });
        UserAccount account = mapper.toEntity(request);
        account.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        repository.save(account);
        kafkaTemplate.send("user-events", new UserEvent(account.getId(), "CREATED", OffsetDateTime.now()));
        return mapper.toDto(account);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "users", key = "#id")
    public UserDto findById(UUID id) {
        return mapper.toDto(repository.findById(id).orElseThrow(() -> new UserNotFoundException("User %s not found".formatted(id))));
    }

    @Transactional
    @CacheEvict(cacheNames = "users", key = "#id")
    public UserDto update(UUID id, UpdateUserRequest request) {
        UserAccount account = repository.findById(id).orElseThrow(() -> new UserNotFoundException("User %s not found".formatted(id)));
        mapper.update(account, request);
        repository.save(account);
        kafkaTemplate.send("user-events", new UserEvent(account.getId(), "UPDATED", OffsetDateTime.now()));
        return mapper.toDto(account);
    }

    @Transactional(readOnly = true)
    public AuthResponse authenticate(AuthRequest request) {
        UserAccount account = repository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Invalid credentials provided."));
        if (!passwordEncoder.matches(request.getPassword(), account.getPasswordHash())) {
            throw new UserNotFoundException("Invalid credentials. Password mismatch");
        }
        String token = jwtTokenProvider.createToken(account.getId(), account.getRole(), Map.of("fullName", account.getFullName()));
        String refreshToken = jwtTokenProvider.createToken(account.getId(), account.getRole(), Map.of("type", "refresh"));
        return AuthResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .expiresAt(OffsetDateTime.now().plusSeconds(3600))
                .build();
    }
}
