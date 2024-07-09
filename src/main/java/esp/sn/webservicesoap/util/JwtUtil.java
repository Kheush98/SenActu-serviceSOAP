package esp.sn.webservicesoap.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY_STRING = "JVPk3cFhsL6mBd3eZjEzltCQz0qN3hFOSpxyqowHhM0=";
    private static final byte[] SECRET_KEY_BYTES = Base64.getDecoder().decode(SECRET_KEY_STRING);
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_BYTES);
    private static final long EXPIRATION_TIME_MS = 864_000_000; // 10 days

    public static String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME_MS);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public static void validateToken(String token) {
        Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
    }

    private static SecretKey getSigningKey() {
        // Use a symmetric key for signing JWT
        return SECRET_KEY;
    }
}
