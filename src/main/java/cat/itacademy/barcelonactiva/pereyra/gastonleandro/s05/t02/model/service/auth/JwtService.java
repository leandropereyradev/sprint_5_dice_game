package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long expirationTime;

    @Autowired
    private InvalidTokenService invalidTokenService;

    public String getToken(UserDetails player) {
        return getToken(new HashMap<>(), player);
    }

    private String getToken(Map<String, Object> extraClaims, UserDetails player) {
        String role = player.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("");

        return Jwts
                .builder()
                .claim("nickname", player.getUsername())
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = secretKey.getBytes();

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String nickName = getNickNameFromToken(token);

        return (nickName.equals(userDetails.getUsername()) && !isTokenExpired(token) && !invalidTokenService.isTokenInvalid(token));
    }

    public String getNickNameFromToken(String token) {
        return getClaim(token, claims -> claims.get("nickname", String.class));
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

    public String getExpiredToken() {
        return Jwts.builder()
                .setExpiration(new Date(0))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }
}
