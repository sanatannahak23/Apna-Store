package com.apnaStore.user_service.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenService {

    @Value("${spring.security.secret}")
    private String SECRET_KEY;

    public String getToken(String userName, String role, String userRef) {
        Map<String, Object> map = new HashMap<>();
        map.put("role", role);
        map.put("userRef", userRef);
        return generateToken(userName, map);
    }

    private String generateToken(String userName, Map<String, Object> map) {
        return Jwts
                .builder()
                .claims(map)
                .subject(userName)
                .header().empty().add("typ", "jwt")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .signWith(getSignKey())
                .compact();
    }

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}
