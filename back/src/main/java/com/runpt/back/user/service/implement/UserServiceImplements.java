package com.runpt.back.user.service.implement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.runpt.back.global.dto.KakaoUserInfo;
import com.runpt.back.global.dto.ResponseDto;
import com.runpt.back.global.helper.KakaoOauthHelper;
import com.runpt.back.user.dto.request.KakaoLoginRequestDto;
import com.runpt.back.user.dto.response.KakaoLoginResponseDto;
import com.runpt.back.user.entity.UserEntity;
import com.runpt.back.user.repository.UserRepository;
import com.runpt.back.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImplements implements UserService {

    private final UserRepository userRepository;
    private final KakaoOauthHelper kakaoOauthHelper;

    // application.yml 에서 불러오기
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URL;

    @Override
    public ResponseEntity<? super KakaoLoginResponseDto> kakaoLogin(KakaoLoginRequestDto dto) {

        try {
            String code = dto.getCode();
            if (code == null || code.isEmpty()) {
                return ResponseDto.badRequest();
            }

            // 🔥 1. KakaoUserInfo 가져오기 (id + nickname)
            KakaoUserInfo info = kakaoOauthHelper.getKakaoUserInfo(code, KAKAO_REDIRECT_URL);
            if (info == null) {
                return KakaoLoginResponseDto.databaseError();
            }

            String kakaoId = info.getId();
            String nickname = info.getNickname();

            // 🔥 2. 기존 유저인지 확인
            UserEntity user = userRepository.findByOauthProviderAndOauthUid("kakao", kakaoId);

            boolean isNew = false;

            // ❗ 3. 신규 회원 — 회원가입 처리
            if (user == null) {
                isNew = true;

                user = new UserEntity();
                user.setOauthProvider("kakao");
                user.setOauthUid(kakaoId);

                // 카카오 닉네임 저장 (없을 수도 있으니 null 체크)
                if (nickname != null) {
                    user.setNickname(nickname);
                }else{
                    user.setNickname("닉네임 없음");
                }

                userRepository.save(user);
            }

            // 🔥 4. 로그인 성공 — uid 반환
            long uid = user.getId();
            return KakaoLoginResponseDto.kakaoLoginSuccess(uid, isNew);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
    }
}
