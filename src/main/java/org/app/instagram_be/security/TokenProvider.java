package org.app.instagram_be.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class TokenProvider {
    //    secret key for jwt
    private final static String SECRET_KEY = "SecretkeYformYApplication2024";
    private final static Logger LOGGER = LoggerFactory.getLogger(TokenProvider.class);
    private final static Random RANDOM = new SecureRandom();

    public String generateAccessToken(Authentication authentication) {
        String role = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(3600);
        System.out.println("Test: " + getSecretKey());
        return Jwts.builder()
                .claim("role", role)
                .subject(authentication.getName())
                .expiration(Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(getSecretKey())
                .compact();
    }

    // get authentication for validate jwt
    public Authentication getAuthentication(String token) {
        if (token.isEmpty()) {
            return null;
        }
        System.out.println("test2: " + getSecretKey());
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        List<GrantedAuthority> roles = Arrays.stream(claims.get("role").toString().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        User user = new User(claims.getSubject(), "", roles);
        return new UsernamePasswordAuthenticationToken(user, null, roles);
    }

    // get secret key
    public SecretKey getSecretKey() {
        byte[] bytes = generateSecretKeyString(SECRET_KEY).getBytes(StandardCharsets.UTF_8);
        try {
            return new SecretKeySpec(bytes, "HmacSHA256");
        } catch (Exception e) {
            LOGGER.error("Fail: " + e.getMessage());
        }
        return null;
    }

    // generate secret key string
    public String generateSecretKeyString(String key) {
        StringBuilder builder = new StringBuilder(key);
        while (builder.toString().getBytes().length < 32) {
            builder.append(key.charAt(key.length()-1));
        }
        System.out.println("Secret Key: " + builder.toString());
        return builder.toString();
    }

}
