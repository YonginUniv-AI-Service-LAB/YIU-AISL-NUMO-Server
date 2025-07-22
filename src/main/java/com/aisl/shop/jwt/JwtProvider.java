package com.aisl.shop.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    private Key signingKey;

    private final long accessTokenExpiration = 1000L * 60 * 60;            // 1시간
    private final long refreshTokenExpiration = 1000L * 60 * 60 * 24 * 14; // 14일

    @PostConstruct
    public void init() {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // ✅ Access Token 생성 (userId + role)
    public String generateAccessToken(Long userId, String role) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // userId를 subject에 저장
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Refresh Token 생성 (email 기반)
    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email) // 이메일은 refresh token에서만 사용
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Access Token에서 userId 추출
    public Long getUserId(String token) {
        try {
            return Long.parseLong(extractAllClaims(token).getSubject());
        } catch (Exception e) {
            log.warn("[JwtProvider] userId 추출 실패: {}", e.getMessage());
            return null;
        }
    }

    // ✅ Access Token에서 role 추출
    public String getRole(String token) {
        try {
            return extractAllClaims(token).get("role", String.class);
        } catch (Exception e) {
            log.warn("[JwtProvider] role 추출 실패: {}", e.getMessage());
            return null;
        }
    }

    // ✅ Refresh Token에서 email 추출
    public String getEmail(String token) {
        try {
            return extractAllClaims(token).getSubject();
        } catch (Exception e) {
            log.warn("[JwtProvider] email 추출 실패: {}", e.getMessage());
            return null;
        }
    }

    // ✅ 유효성 검사
    public boolean isValidToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("[JwtProvider] 만료된 토큰: {}", e.getMessage());
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("[JwtProvider] 유효하지 않은 토큰: {}", e.getMessage());
            return false;
        }
    }

    // ✅ 내부 Claims 추출
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
