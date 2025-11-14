package ai.shopsense.gateway.security;

import ai.shopsense.shared.security.JwtTokenProvider;
import ai.shopsense.shared.security.UserContext;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GatewayJwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        if (!jwtTokenProvider.isValid(token)) {
            return Mono.error(new BadCredentialsException("Invalid JWT"));
        }
        Claims claims = jwtTokenProvider.parseClaims(token);
        UUID userId = UUID.fromString(claims.getSubject());
        String role = claims.get("role", String.class);
        var principal = new UserContext(userId, role);
        return Mono.just(new UsernamePasswordAuthenticationToken(principal, token, List.of(new SimpleGrantedAuthority(role))));
    }
}
