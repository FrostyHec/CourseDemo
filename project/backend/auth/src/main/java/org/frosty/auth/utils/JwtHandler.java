package org.frosty.auth.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.frosty.auth.entity.AuthInfo;
import org.frosty.auth.exception.InvalidTokenException;

import java.security.Key;
import java.util.Date;
public class JwtHandler {
    private final long expiration;

    private final Key key;

    public JwtHandler(String secret, long expiration) {
        this.expiration = expiration;
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createToken(AuthInfo auth) {
        return Jwts.builder()
                .setSubject(TokenUtils.authInfoToString(auth))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims getClaimsFromToken(String token) throws InvalidTokenException {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid token", e);
        }
    }

    public boolean isTokenExpired(Claims claims) {
        return claims
                .getExpiration()
                .before(new Date());
    }

    public AuthInfo getToken(Claims claims) {
        return TokenUtils.authInfoFromString(claims.getSubject());
    }
}
