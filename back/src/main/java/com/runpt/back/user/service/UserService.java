package com.runpt.back.user.service;

import com.runpt.back.user.entity.User;
import com.runpt.back.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findByOauth(String provider, String oauthUid) {
        return userRepository.findByOauthProviderAndOauthUid(provider, oauthUid);
    }

    public User createUser(String provider, String oauthUid) {
        User newUser = User.builder()
                .oauthProvider(provider)
                .oauthUid(oauthUid)
                .build();

        return userRepository.save(newUser);
    }

    /**
     * 유저 조회/생성 결과를 함께 반환하는 record
     */
    public record UserResult(User user, boolean isNewUser) {}

    /**
     * 있다면 가져오고, 없다면 생성 + 신규 여부 반환
     */
    public UserResult findOrCreate(String provider, String oauthUid) {
        Optional<User> exist = userRepository.findByOauthProviderAndOauthUid(provider, oauthUid);

        if (exist.isPresent()) {
            return new UserResult(exist.get(), false);
        }

        // 신규 생성
        User newUser = createUser(provider, oauthUid);
        return new UserResult(newUser, true);
    }
}