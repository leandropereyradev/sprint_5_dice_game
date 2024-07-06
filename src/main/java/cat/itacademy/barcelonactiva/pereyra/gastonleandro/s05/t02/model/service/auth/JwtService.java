package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "kshfglkhsl34hbDSFagdjksfSFDGrefDdggsfgsfgjj5kblkjflkg";
    private static final long EXPIRATION_TIME_MS = 1000 * 60 * 60 * 24;

    public String getToken(UserDetails player) {
        return getToken(new HashMap<>(), player);
    }

    private String getToken(Map<String, Object> extraClaims, UserDetails player) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(player.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String nickName = getNickNameFromToken(token);

        return (nickName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String getNickNameFromToken(String token) {
        return getClaim(token, Claims :: getSubject);
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
        return getClaim(token, Claims :: getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());

    }
}
