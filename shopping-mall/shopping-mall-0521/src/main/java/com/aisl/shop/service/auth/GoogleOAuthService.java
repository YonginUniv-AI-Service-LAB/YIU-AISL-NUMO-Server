package com.aisl.shop.service.auth;

import com.aisl.shop.entity.User;
import com.aisl.shop.jwt.JwtProvider;
import com.aisl.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    private final String GOOGLE_TOKEN_INFO_URL = "https://oauth2.googleapis.com/tokeninfo?id_token=";

    public String loginWithGoogle(String idToken) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> userInfo = restTemplate.getForObject(GOOGLE_TOKEN_INFO_URL + idToken, Map.class);

        String email = userInfo.get("email");
        String sub = userInfo.get("sub"); // Google 고유 ID
        String name = userInfo.get("name"); // Optional

        Optional<User> userOpt = userRepository.findByEmail(email);

        User user = userOpt.orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name != null ? name : "GoogleUser"); // name 없을 경우 기본값
            newUser.setProvider(User.Provider.GOOGLE); // ✅ enum 값으로 지정
            newUser.setProviderId(sub);
            newUser.setRole(User.Role.USER);           // ✅ enum 값으로 지정
            newUser.setNickname("google_" + sub.substring(0, 6)); // 닉네임 기본값 예시
            newUser.setPhone("010-0000-0000"); // 임시 기본값 (나중에 수정 가능)
            return userRepository.save(newUser);
        });

        return jwtProvider.generateToken(user.getId());
    }
}
