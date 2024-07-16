package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.security;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.enums.Role;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.exception.AccessDeniedException;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.exception.InvalidTokenException;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.exception.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.domain.player.PlayerEntity;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.repository.player.PlayerRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long expirationTime;

    @Autowired
    private PlayerRepository playerRepository;

    private final Set<String> invalidTokens = new HashSet<>();

    public String getToken(UserDetails player) {
        return getToken(new HashMap<>(), player);
    }

    private String getToken(Map<String, Object> extraClaims, UserDetails player) {
        String role = player.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("");

        if (player instanceof PlayerEntity playerEntity)
            return Jwts
                    .builder()
                    .claim("email", player.getUsername())
                    .claim("nickname", playerEntity.getNickName())
                    .claim("role", role)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                    .signWith(getKey(), SignatureAlgorithm.HS256)
                    .compact();

        else throw new IllegalArgumentException("UserDetails is not an instance of PlayerEntity");

    }

    private Key getKey() {
        byte[] keyBytes = secretKey.getBytes();

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = getEmailFromToken(token);

        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token)
                && !isTokenInvalid(token));
    }

    public String getEmailFromToken(String token) {
        return getClaim(token, claims -> claims.get("email", String.class));
    }

    private Claims getAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);

        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    public void invalidateToken(String token) {
        invalidTokens.add(token);
    }

    public boolean isTokenInvalid(String token) {
        return invalidTokens.contains(token);
    }

    public void verifyEmailMatch(Long playerId, HttpServletRequest request) {
        PlayerEntity tokenPlayer = getPlayerFromToken(request);

        if (!tokenPlayer.getRole().equals(Role.ROLE_ADMIN)) {
            PlayerEntity player = playerRepository.findById(playerId)
                    .orElseThrow(() -> new PlayerNotFoundException("Player not found"));

            if (!player.getEmail().equals(tokenPlayer.getEmail()))
                throw new AccessDeniedException("Access denied: You are trying to access a user that does not correspond to your credential.");
        }
    }

    public String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer "))
            throw new InvalidTokenException("Missing or invalid Authorization header");

        return authorizationHeader.substring(7);
    }

    public PlayerEntity getPlayerFromToken(HttpServletRequest request) {
        String token = extractToken(request);
        String email = getEmailFromToken(token);

        return playerRepository.findByEmail(email)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found"));
    }
}
