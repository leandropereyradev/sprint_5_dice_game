package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class InvalidTokenService {

    private final Set<String> invalidTokens = new HashSet<>();

    public void invalidateToken(String token) {
        invalidTokens.add(token);
    }

    public boolean isTokenInvalid(String token) {
        return invalidTokens.contains(token);
    }
}
