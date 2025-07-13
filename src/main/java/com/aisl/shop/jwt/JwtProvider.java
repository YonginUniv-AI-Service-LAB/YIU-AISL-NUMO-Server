package com.aisl.shop.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    private Key signingKey;

    private final long accessTokenExpiration = 1000L * 60 * 60;        // 1시간
    private final long refreshTokenExpiration = 1000L * 60 * 60 * 24 * 14; // 14일

    @PostConstruct
    public void init() {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // 사용자 ID만으로 생성 (기본 USER 권한)
    public String generateAccessToken(Long userId) {
        return generateAccessToken(userId, "USER");
    }


    // ✅ Access Token 생성 (userId + role)
    public String generateAccessToken(Long userId, String role) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Refresh Token 생성 (email 기반)
    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
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
            System.out.println("[JwtProvider] userId 추출 실패: " + e.getMessage());
            return null;
        }
    }

    // ✅ Access Token에서 role 추출
    public String getRole(String token) {
        try {
            return extractAllClaims(token).get("role", String.class);
        } catch (Exception e) {
            System.out.println("[JwtProvider] role 추출 실패: " + e.getMessage());
            return null;
        }
    }

    // ✅ Refresh Token에서 email 꺼내기
    public String getEmail(String token) {
        try {
            return extractAllClaims(token).getSubject();
        } catch (Exception e) {
            System.out.println("[JwtProvider] email 추출 실패: " + e.getMessage());
            return null;
        }
    }

    // ✅ 유효성 검사
    public boolean isValidToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("[JwtProvider] 유효하지 않은 토큰: " + e.getMessage());
            return false;
        }
    }

    // ✅ 내부 Claims 추출 (공통)
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
