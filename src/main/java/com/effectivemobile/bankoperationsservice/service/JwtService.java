package com.effectivemobile.bankoperationsservice.service;

import com.effectivemobile.bankoperationsservice.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractClaim(token, Claims::getSubject);
        return (username.equals(userDetails.getUsername())) && isTokenExpired(token);
    }

    public boolean isTokenValid(String token, User user) {
        final String username = extractClaim(token, Claims::getSubject);
        return (username.equals(converUserToUserDetails(user).getUsername())) && isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return !extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public String generateToken(User user) {
        return buildToken(new HashMap<>(), converUserToUserDetails(user), jwtExpiration);
    }

    public String extractLogin(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateRefreshToken(
            User user
    ) {
        return buildToken(new HashMap<>(), converUserToUserDetails(user), refreshExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuer(String.valueOf(new Date(System.currentTimeMillis())))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private UserDetails converUserToUserDetails(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(), user.getPassword(), new ArrayList<>()
        );
    }
}