package com.aisl.shop.service.auth;

import com.aisl.shop.entity.User;
import com.aisl.shop.repository.UserRepository;
import com.aisl.shop.exception.ConflictException;
import com.aisl.shop.exception.UnauthorizedException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpTransport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

    private final UserRepository userRepository;

    @Value("${oauth.google.client-id}")
    private String clientId;

    public User loginWithGoogle(String idTokenString) {
        try {
            HttpTransport transport = Utils.getDefaultTransport();
            JsonFactory jsonFactory = Utils.getDefaultJsonFactory();

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                log.warn("❌ 유효하지 않은 Google ID Token입니다.");
                throw new UnauthorizedException("❌ 유효하지 않은 Google ID Token입니다.");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String sub = payload.getSubject();

            log.info("[Google Login] 사용자 이메일: {}, Google sub: {}", email, sub);

            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                User existingUser = userOpt.get();

                if (existingUser.getProvider() != User.Provider.GOOGLE) {
                    log.warn("[Google Login] 이메일 중복 - 다른 방식으로 가입된 사용자: {}", email);
                    throw new ConflictException("이미 다른 방식(일반 회원가입)으로 가입된 이메일입니다.");
                }

                log.info("[Google Login] 기존 구글 사용자 로그인 성공: {}", email);
                return existingUser;
            }

            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name != null ? name : "GoogleUser");
            newUser.setProvider(User.Provider.GOOGLE);
            newUser.setProviderId(sub);
            newUser.setRole(User.Role.USER);
            newUser.setNickname("google_" + sub.substring(0, 6));
            newUser.setPhone("010-0000-0000");

            User savedUser = userRepository.save(newUser);
            log.info("[Google Login] 신규 구글 사용자 가입 완료: {}", email);
            return savedUser;

        } catch (UnauthorizedException | ConflictException e) {
            throw e;
        } catch (Exception e) {
            log.error("Google 로그인 처리 중 예외 발생", e);
            throw new RuntimeException("Google 로그인 처리 중 서버 오류가 발생했습니다.");
        }
    }
}
