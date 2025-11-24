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

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URL;

    @Override
    public ResponseEntity<? super KakaoLoginResponseDto> kakaoLogin(KakaoLoginRequestDto dto) {
        long uid = 0;
        boolean isNew = false;
        String nickname = null;

        try {
            String code = dto.getCode();
            if (code == null || code.isEmpty()) {
                return ResponseDto.badRequest();
            }

            // 🔥 Log - 받은 code 출력
            System.out.println("[KAKAO LOGIN] Received code = " + code);

            // 1) KakaoUserInfo 가져오기
            KakaoUserInfo info = kakaoOauthHelper.getKakaoUserInfo(code, KAKAO_REDIRECT_URL);
            if (info == null) {
                System.out.println("[KAKAO LOGIN] KakaoUserInfo is NULL");
                return KakaoLoginResponseDto.databaseError();
            }

            String kakaoId = info.getId();
            nickname = info.getNickname();

            // 🔥 Log - 카카오 정보 출력
            System.out.println("[KAKAO LOGIN] KakaoId = " + kakaoId);
            System.out.println("[KAKAO LOGIN] Nickname = " + nickname);

            // 2) 기존 유저인지 확인
            UserEntity user = userRepository.findByOauthProviderAndOauthUid("kakao", kakaoId);

            // 3) 신규 회원 가입 처리
            if (user == null) {
                isNew = true;

                System.out.println("[KAKAO LOGIN] 신규 회원입니다. 카카오 정보로 회원가입 진행.");

                user = new UserEntity();
                user.setOauthProvider("kakao");
                user.setOauthUid(kakaoId);

                if (nickname != null) {
                    user.setNickname(nickname);
                } else {
                    user.setNickname("닉네임 없음");
                }

                userRepository.save(user);

                System.out.println("[KAKAO LOGIN] 신규 회원 저장 완료.");
            } else {
                System.out.println("[KAKAO LOGIN] 기존 회원 로그인 처리.");
            }

            // 4) 로그인 성공 → uid 가져오기
            uid = user.getId();

            // 🔥 Log - 최종 정보 출력
            System.out.println("[KAKAO LOGIN] Login Success → uid = " + uid);
            System.out.println("[KAKAO LOGIN] isNew = " + isNew);
            System.out.println("[KAKAO LOGIN] Final Nickname = " + nickname);

        } catch (Exception e) {
            System.out.println("[KAKAO LOGIN] ERROR OCCURRED: " + e.getMessage());
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return KakaoLoginResponseDto.kakaoLoginSuccess(uid, isNew, nickname);
    }
}
