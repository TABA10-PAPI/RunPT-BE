package com.runpt.back.user.service.implement;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.runpt.back.global.dto.KakaoUserInfo;
import com.runpt.back.global.dto.ResponseDto;
import com.runpt.back.global.helper.KakaoOauthHelper;
import com.runpt.back.user.entity.RunningSessionEntity;
import com.runpt.back.user.entity.TierEntity;
import com.runpt.back.user.dto.request.GetMyPageRequestDto;
import com.runpt.back.user.dto.request.JoinRequestDto;
import com.runpt.back.user.dto.request.KakaoLoginRequestDto;
import com.runpt.back.user.dto.request.SaveRunningRequestDto;
import com.runpt.back.user.dto.request.RunningToAIRequestDto;

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

    @Override
    public ResponseEntity<? super KakaoLoginResponseDto> kakaoLogin(KakaoLoginRequestDto dto) {
        long uid = 0;
        boolean fresh = false;
        String nickname = null;

        try {
            if (dto.getAccessToken() == null || dto.getAccessToken().isEmpty()) {
                return ResponseDto.badRequest();
            }

            KakaoUserInfo info = kakaoOauthHelper.getUserInfoFromToken(dto.getAccessToken());
            if (info == null) {
                return KakaoLoginResponseDto.databaseError();
            }

            String kakaoId = info.getId();
            nickname = info.getNickname();

            UserEntity user = userRepository.findByOauthProviderAndOauthUid("kakao", kakaoId);

            if (user == null) {
                fresh = true;
                user = new UserEntity();
                user.setOauthProvider("kakao");
                user.setOauthUid(kakaoId);
                user.setNickname(nickname != null ? nickname : "닉네임 없음");
                userRepository.save(user);
            }

            uid = user.getId();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return KakaoLoginResponseDto.kakaoLoginSuccess(uid, fresh, nickname);
    }


    @Override
    public ResponseEntity<? super JoinResponseDto> join(JoinRequestDto dto) {

        try {
            UserEntity user = userRepository.findById(dto.getUid());
            if (user == null) return ResponseDto.badRequest();

            user.setNickname(dto.getNickname());
            user.setAge(dto.getAge());
            user.setGender("M");
            user.setHeight(dto.getHeight());
            user.setWeight(dto.getWeight());
            userRepository.save(user);

            TierEntity tier = new TierEntity(dto.getUid());
            tierRepository.save(tier);

            return JoinResponseDto.joinSuccess(user);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
    }


    @Override
    public ResponseEntity<? super GetMyPageResponseDto> getMyPage(GetMyPageRequestDto dto) {
        try {
            UserEntity user = userRepository.findById(dto.getUid());
            if (user == null) return ResponseDto.badRequest();

            TierEntity tier = tierRepository.findByUid(dto.getUid());
            List<RunningSessionEntity> list =
                    runningSessionRepository.findByUidOrderByDateDesc(dto.getUid());

            List<RunningSessionEntity> recent = list.stream()
                    .limit(5)
                    .collect(Collectors.toList());

            return GetMyPageResponseDto.getMyPageSuccess(user, tier, recent);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
    }


    @Override
    public ResponseEntity<? super SaveRunningResponseDto> saveRunning(SaveRunningRequestDto dto) {

        try {
            // 1) 러닝 기록 저장
            RunningSessionEntity session = new RunningSessionEntity();
            session.setUid(dto.getUid());
            session.setDistance(dto.getDistance());
            session.setDate(dto.getDate());
            session.setDurationSec(dto.getDurationSec());
            session.setHeartRateAvg(dto.getHeartRateAvg());
            session.setPace(dto.getPace());
            runningSessionRepository.save(session);

            // 2) 티어 엔티티 조회
            TierEntity tier = tierRepository.findByUid(dto.getUid());
            if (tier == null) {
                tier = new TierEntity(dto.getUid());
            }

            boolean isShort = dto.getDistance() <= 10000;

            // 3) 이번 러닝의 티어 계산 = 초 단위 페이스
            TierCalculator.Tier newTier = isShort
                    ? TierCalculator.calculateShortDistanceTier(dto.getPace())
                    : TierCalculator.calculateLongDistanceTier(dto.getPace());

            // 4) best time = pace 로 저장
            int newBestTime = dto.getPace();

            if (isShort) {

                TierCalculator.Tier oldTier = tier.getShortTierRank() != null
                        ? TierCalculator.Tier.valueOf(tier.getShortTierRank()) : null;

                if (oldTier == null ||
                        TierCalculator.getTierPriority(newTier) > TierCalculator.getTierPriority(oldTier)) {
                    tier.setShortTierRank(newTier.name());
                }

                if (tier.getShortBestTime() == 0 || newBestTime < tier.getShortBestTime()) {
                    tier.setShortBestTime(newBestTime);
                }

            } else {

                TierCalculator.Tier oldTier = tier.getLongTierRank() != null
                        ? TierCalculator.Tier.valueOf(tier.getLongTierRank()) : null;

                if (oldTier == null ||
                        TierCalculator.getTierPriority(newTier) > TierCalculator.getTierPriority(oldTier)) {
                    tier.setLongTierRank(newTier.name());
                }

                if (tier.getLongBestTime() == 0 || newBestTime < tier.getLongBestTime()) {
                    tier.setLongBestTime(newBestTime);
                }
            }

            tierRepository.save(tier);

            // 5) AI 서버로 러닝 데이터 전송
            sendRunningToAi(dto);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return SaveRunningResponseDto.saveRunningSuccess();
    }


    private void sendRunningToAi(SaveRunningRequestDto dto) {
        try {
            String url = "http://13.124.197.160:8000/running/save";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            RunningToAIRequestDto aiDto = new RunningToAIRequestDto(
                    dto.getUid(),
                    dto.getDate().toString(),
                    dto.getDistance(),
                    String.valueOf(dto.getPace()),
                    dto.getDurationSec(),
                    dto.getHeartRateAvg()
            );

            HttpEntity<RunningToAIRequestDto> entity = new HttpEntity<>(aiDto, headers);
            RestTemplate rt = new RestTemplate();

            String res = rt.postForObject(url, entity, String.class);

            System.out.println("[AI RUNNING SEND] SUCCESS → " + res);

        } catch (Exception e) {
            System.out.println("[AI RUNNING SEND] ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
