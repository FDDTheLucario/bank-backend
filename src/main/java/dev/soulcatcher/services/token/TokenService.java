package dev.soulcatcher.services.token;

import dev.soulcatcher.dtos.Principal;
import dev.soulcatcher.exceptions.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {
    private final JwtConfig jwtConfig;
    private static final String TRANSFORMATION = "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING";

    public TokenService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }
    public String generateToken(Principal subject) {
        long now = System.currentTimeMillis();
        long timeout = jwtConfig.getExpiration();

        return Jwts.builder()
                .setIssuer("bank-project")
                .claim("id", subject.getAuthUserId())
                .claim("username", subject.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + timeout))
                .signWith(jwtConfig.getSigningKey(), jwtConfig.getSignatureAlgo())
                .compact();
    }
    public Principal extractToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new UnauthorizedException();
        }
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtConfig.getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return new Principal(claims.get("id", String.class),
                claims.get("username", String.class));
    }
}
