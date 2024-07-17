package org.app.instagram_be.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TokenProvider {
//    secret key for jwt
    private final static String SECRET_KEY = "qwefjsahshscdkxcvl432";

    public String generateAccessToken(Authentication authentication) {
        String role = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(30);
        return Jwts.builder()
                .claim("role",role)
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
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        List<GrantedAuthority> roles = Arrays.stream(claims.get("roles").toString().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        User user = new User(claims.getSubject(),"",roles);
        return new UsernamePasswordAuthenticationToken(user,null,roles);
    }

    // get secret key
    public SecretKey getSecretKey() {
        byte[] bytes = Base64.getDecoder()
                .decode(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(bytes, "HmacSHA512");
    }

}
