package com.runpt.back.user.service.implement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.runpt.back.global.dto.KakaoUserInfo;
import com.runpt.back.global.dto.ResponseDto;
import com.runpt.back.global.helper.KakaoOauthHelper;
import com.runpt.back.user.entity.RunningSessionEntity;
import com.runpt.back.user.entity.TierEntity;
import com.runpt.back.user.dto.request.GetMyPageRequestDto;
import com.runpt.back.user.dto.request.JoinRequestDto;
import com.runpt.back.user.dto.request.KakaoLoginRequestDto;
import com.runpt.back.user.dto.request.SaveRunningRequestDto;
import com.runpt.back.user.dto.response.GetMyPageResponseDto;
import com.runpt.back.user.dto.response.JoinResponseDto;
import com.runpt.back.user.dto.response.KakaoLoginResponseDto;
import com.runpt.back.user.dto.response.SaveRunningResponseDto;
import com.runpt.back.user.entity.UserEntity;
import com.runpt.back.user.repository.TierRepository;
import com.runpt.back.user.repository.UserRepository;
import com.runpt.back.user.repository.RunningSessionRepository;
import com.runpt.back.user.service.UserService;
import com.runpt.back.user.util.TierCalculator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImplements implements UserService {

    private final UserRepository userRepository;
    private final TierRepository tierRepository;
    private final RunningSessionRepository runningSessionRepository;
    private final KakaoOauthHelper kakaoOauthHelper;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URL;

    @Override
    public ResponseEntity<? super KakaoLoginResponseDto> kakaoLogin(KakaoLoginRequestDto dto) {
        long uid = 0;
        boolean isNew = false;
        String nickname = null;

        try {
            String accessToken = dto.getAccessToken();
            if (accessToken == null || accessToken.isEmpty()) {
                System.out.println("[KAKAO LOGIN] accessToken is NULL");
                return ResponseDto.badRequest();
            }

            // 🔥 Log - 받은 accessToken 출력
            System.out.println("[KAKAO LOGIN] Received accessToken = " + accessToken);

            // 1) AccessToken → KakaoUserInfo 가져오기
            KakaoUserInfo info = kakaoOauthHelper.getUserInfoFromToken(accessToken);
            if (info == null) {
                System.out.println("[KAKAO LOGIN] KakaoUserInfo is NULL");
                return KakaoLoginResponseDto.databaseError();
            }

            String kakaoId = info.getId();
            nickname = info.getNickname();

            // 🔥 Log - 카카오 정보 출력
            System.out.println("[KAKAO LOGIN] KakaoId = " + kakaoId);
            System.out.println("[KAKAO LOGIN] Nickname = " + nickname);

            // 2) 기존 유저 확인
            UserEntity user = userRepository.findByOauthProviderAndOauthUid("kakao", kakaoId);

            // 3) 신규 회원 가입
            if (user == null) {
                isNew = true;
                System.out.println("[KAKAO LOGIN] 신규 회원 → 회원가입 실행");

                user = new UserEntity();
                user.setOauthProvider("kakao");
                user.setOauthUid(kakaoId);
                user.setNickname(nickname != null ? nickname : "닉네임 없음");

                userRepository.save(user);
                System.out.println("[KAKAO LOGIN] 신규 회원 저장 완료");
            } else {
                System.out.println("[KAKAO LOGIN] 기존 회원 로그인");
            }

            // 4) uid 반환
            uid = user.getId();

            System.out.println("[KAKAO LOGIN] Login Success → uid = " + uid);
            System.out.println("[KAKAO LOGIN] isNew = " + isNew);

        } catch (Exception e) {
            System.out.println("[KAKAO LOGIN] ERROR: " + e.getMessage());
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return KakaoLoginResponseDto.kakaoLoginSuccess(uid, isNew, nickname);
    }


    @Override
    public ResponseEntity<? super JoinResponseDto> join(JoinRequestDto dto) {
        UserEntity user = null;
        try {
            user = userRepository.findById(dto.getUid());
            if (user == null) {
                System.out.println("[USER JOIN] User not found with uid: " + dto.getUid());
                return ResponseDto.badRequest();
            }
            user.setNickname(dto.getNickname());
            user.setAge(dto.getAge());
            user.setGender(dto.getGender());
            user.setHeight(dto.getHeight());
            user.setWeight(dto.getWeight());
            userRepository.save(user);
            TierEntity tier = new TierEntity(dto.getUid());
            tierRepository.save(tier);
            
        } catch (Exception e) {
            System.out.println("[USER JOIN] ERROR OCCURRED: " + e.getMessage());
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return JoinResponseDto.joinSuccess(user);
    }

    @Override
    public ResponseEntity<? super GetMyPageResponseDto> getMyPage(GetMyPageRequestDto dto) {
        UserEntity user;
        TierEntity tier;
        List<RunningSessionEntity> recentRecords = new ArrayList<>();
        try {
            user = userRepository.findById(dto.getUid());
            if (user == null) {
                System.out.println("[GET MY PAGE] User not found with uid: " + dto.getUid());
                return ResponseDto.badRequest();
            }
            tier = tierRepository.findById(dto.getUid()).orElse(null);
            // 전체 러닝 기록 가져오기
            List<RunningSessionEntity> allRecords = runningSessionRepository.findByUidOrderByDateDesc(dto.getUid());

            // 최근 5개만 추출 (비어있으면 empty list 유지)
            if (allRecords != null && !allRecords.isEmpty()) {
                recentRecords = allRecords.stream()
                        .limit(5)
                        .collect(Collectors.toList());
            }
            
        } catch (Exception e) {
            System.out.println("[GET MY PAGE] ERROR OCCURRED: " + e.getMessage());
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        
        return GetMyPageResponseDto.getMyPageSuccess(user, tier, recentRecords);
    }


    @Override
    public ResponseEntity<? super SaveRunningResponseDto> saveRunning(SaveRunningRequestDto dto) {
        
        try {
            RunningSessionEntity session = new RunningSessionEntity();
            session.setUid(dto.getUid());
            session.setDistance(dto.getDistance());
            session.setDate(dto.getDate());
            session.setDurationSec(dto.getDurationSec());
            session.setHeartRateAvg(dto.getHeartRateAvg());
            session.setPace(dto.getPace());
            runningSessionRepository.save(session);

            // 2. 이 유저의 TierEntity 조회 (회원가입 시 생성되어 있다고 가정)
            TierEntity tier = tierRepository.findByUid(dto.getUid());
            if (tier == null) {
                // 이 상황은 원래 없어야 하지만, 혹시 모를 방어 코드
                tier = new TierEntity(dto.getUid());
            }

            // 3. 단거리 / 장거리 구분 (10km 이하: 단거리, 그 이상: 장거리)
            boolean isShort = dto.getDistance() <= 10.0;

            // 4. 이번 러닝의 티어 계산 (pace는 분/km)
            TierCalculator.Tier newTier = isShort
                    ? TierCalculator.calculateShortDistanceTier(dto.getPace())
                    : TierCalculator.calculateLongDistanceTier(dto.getPace());

            int newBestTime = dto.getDurationSec(); // 초 단위 기록

            // 5. 기존 티어/기록과 비교 후 갱신
            if (isShort) {
                // 기존 단거리 티어 (null이면 아직 기록 없음으로 간주)
                TierCalculator.Tier oldTier = tier.getShortTierRank() != null
                        ? TierCalculator.Tier.valueOf(tier.getShortTierRank())
                        : null;

                // 5-1. 티어 갱신: 기존 티어가 없거나, 새 티어가 더 높으면 교체
                if (oldTier == null ||
                        TierCalculator.getTierPriority(newTier) > TierCalculator.getTierPriority(oldTier)) {
                    tier.setShortTierRank(newTier.name());
                }

                // 5-2. 최고 기록 갱신: 0이면 아직 없음, 더 빠른 기록(시간이 더 작음)이면 교체
                if (tier.getShortBestTime() == 0 || newBestTime < tier.getShortBestTime()) {
                    tier.setShortBestTime(newBestTime);
                }

            } else {
                // 기존 장거리 티어
                TierCalculator.Tier oldTier = tier.getLongTierRank() != null
                        ? TierCalculator.Tier.valueOf(tier.getLongTierRank())
                        : null;

                if (oldTier == null ||
                        TierCalculator.getTierPriority(newTier) > TierCalculator.getTierPriority(oldTier)) {
                    tier.setLongTierRank(newTier.name());
                }

                if (tier.getLongBestTime() == 0 || newBestTime < tier.getLongBestTime()) {
                    tier.setLongBestTime(newBestTime);
                }
            }

            // 6. 티어 정보 저장
            tierRepository.save(tier);
        } catch (Exception e) {
            System.out.println("[SAVE RUNNING] ERROR OCCURRED: " + e.getMessage());
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return SaveRunningResponseDto.saveRunningSuccess();
    }
}
