package com.runpt.back.user.service.implement;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.runpt.back.global.dto.KakaoUserInfo;
import com.runpt.back.global.dto.NaverUserInfo;
import com.runpt.back.global.dto.ResponseDto;
import com.runpt.back.global.helper.KakaoOauthHelper;
import com.runpt.back.user.entity.BatteryEntity;
import com.runpt.back.global.helper.NaverOauthHelper;
import com.runpt.back.user.entity.RunningSessionEntity;
import com.runpt.back.user.entity.TierEntity;
import com.runpt.back.user.dto.request.BatteryToAiRequestDto;
import com.runpt.back.user.dto.request.GetMyPageRequestDto;
import com.runpt.back.user.dto.request.JoinRequestDto;
import com.runpt.back.user.dto.request.KakaoLoginRequestDto;
import com.runpt.back.user.dto.request.NaverLoginRequestDto;
import com.runpt.back.user.dto.request.SaveRunningRequestDto;
import com.runpt.back.user.dto.request.RunningToAIRequestDto;

import com.runpt.back.user.dto.response.GetMyPageResponseDto;
import com.runpt.back.user.dto.response.JoinResponseDto;
import com.runpt.back.user.dto.response.KakaoLoginResponseDto;
import com.runpt.back.user.dto.response.SaveRunningResponseDto;
import com.runpt.back.user.dto.response.NaverLoginResponseDto;

import com.runpt.back.user.entity.UserEntity;
import com.runpt.back.user.repository.TierRepository;
import com.runpt.back.user.repository.UserRepository;
import com.runpt.back.user.repository.BatteryRepository;
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
    private final BatteryRepository batteryRepository;
    private final KakaoOauthHelper kakaoOauthHelper;
    private final NaverOauthHelper naverOauthHelper;

    @Override
    public ResponseEntity<? super KakaoLoginResponseDto> kakaoLogin(KakaoLoginRequestDto dto) {
        long uid = 0;
        boolean fresh = false;
        String nickname = null;

        try {
            if (dto.getAccessToken() == null || dto.getAccessToken().isEmpty()) {
                return KakaoLoginResponseDto.invalidAccessToken();
            }

            KakaoUserInfo info = kakaoOauthHelper.getUserInfoFromToken(dto.getAccessToken());
            if (info == null) {
                return KakaoLoginResponseDto.oauthApiError();
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

            getBatteryInfo(uid, dto.getDate());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return KakaoLoginResponseDto.kakaoLoginSuccess(uid, fresh, nickname);
    }

    public ResponseEntity<? super NaverLoginResponseDto> naverLogin(NaverLoginRequestDto dto) {
        long uid = 0;
        boolean fresh = false;
        String nickname = null;

        try {
            if (dto.getAccessToken() == null || dto.getAccessToken().isEmpty()) {
                return NaverLoginResponseDto.invalidAccessToken();
            }

            NaverUserInfo info = naverOauthHelper.getUserInfoFromToken(dto.getAccessToken());
            if (info == null) {
                return NaverLoginResponseDto.oauthApiError();
            }

            String naverId = info.getId();
            nickname = info.getNickname();

            UserEntity user = userRepository.findByOauthProviderAndOauthUid("naver", naverId);

            if (user == null) {
                fresh = true;
                user = new UserEntity();
                user.setOauthProvider("naver");
                user.setOauthUid(naverId);
                user.setNickname(nickname != null ? nickname : "닉네임 없음");
                userRepository.save(user);
            }

            uid = user.getId();

            getBatteryInfo(uid, dto.getDate());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return NaverLoginResponseDto.naverLoginSuccess(uid, fresh, nickname);
    }


    @Override
    public ResponseEntity<? super JoinResponseDto> join(JoinRequestDto dto) {

        try {
            UserEntity user = userRepository.findById(dto.getUid());
            if (user == null) return JoinResponseDto.userNotExitsts();

            if (dto.getAge() <= 0 || dto.getAge() > 150) {
                return ResponseDto.validationFail();
            }

            if (dto.getHeight() <= 0 || dto.getHeight() > 250) {
                return ResponseDto.validationFail();
            }

            if (dto.getWeight() <= 0 || dto.getWeight() > 200) {
                return ResponseDto.validationFail();
            }

            if (!"M".equalsIgnoreCase(dto.getGender()) && !"F".equalsIgnoreCase(dto.getGender())) {
                return ResponseDto.validationFail();
            }
            
            user.setNickname(dto.getNickname());
            user.setAge(dto.getAge());
            user.setGender(dto.getGender());
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
            if (user == null) return GetMyPageResponseDto.userNotExists();

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

            if (dto.getDate() == null) {
                return SaveRunningResponseDto.invalidDateFormat();  
            }

            if (dto.getPace() <= 0 || dto.getDistance() <= 0 || dto.getDurationSec() <= 0 || dto.getHeartRateAvg() < 0) {
                return SaveRunningResponseDto.invalidRunningData(); 
            }
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
            TierCalculator.Tier newTier;
            try {
                newTier = isShort
                        ? TierCalculator.calculateShortDistanceTier(dto.getPace())
                        : TierCalculator.calculateLongDistanceTier(dto.getPace());
            } catch (Exception e) {
                return SaveRunningResponseDto.tierCalculationError();  
            }

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
    // AI 서버에서 배터리 정보 받아와서 DB에 저장 + 엔티티 반환
    private void getBatteryInfo(Long uid, String date) {
        System.out.println("===== [AI BATTERY REQUEST START] =====");
        System.out.println("UID = " + uid);
        System.out.println("DATE = " + date);

        try {
            // 1) 외부 AI URL
            String url = "http://13.124.197.160:8000/battery";

            // 2) HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 3) 요청 바디 DTO 생성
            BatteryToAiRequestDto dto = new BatteryToAiRequestDto(uid, date);

            // 4) HttpEntity 생성
            HttpEntity<BatteryToAiRequestDto> req = new HttpEntity<>(dto, headers);

            // 5) RestTemplate 생성 후 호출
            RestTemplate rt = new RestTemplate();
            JsonNode aiRes = rt.postForObject(url, req, JsonNode.class);

            System.out.println("---- AI RAW RESPONSE ----");
            System.out.println(aiRes == null ? "NULL RESPONSE" : aiRes.toPrettyString());

            if (aiRes == null) {
                throw new RuntimeException("AI 응답이 NULL입니다.");
            }

            // 6) JSON 파싱
            if (!aiRes.has("battery_score")) {
                throw new RuntimeException("AI 응답에 battery_score 필드가 없습니다.");
            }
            float battery = aiRes.get("battery_score").floatValue();

            JsonNode recNode = aiRes.get("recommendations");
            if (recNode == null || !recNode.isArray()) {
                throw new RuntimeException("AI 응답에 recommendations 배열이 없습니다.");
            }

            String recommendationsJson = recNode.toString();

            // 7) DB UPSERT
            BatteryEntity entity = batteryRepository.findByUid(uid);
            if (entity == null) {
                entity = new BatteryEntity();
                entity.setUid(uid);
                entity.setDate(date);
                
            }


            entity.setBattery(battery);
            entity.setRecommendationsJson(recommendationsJson);

            batteryRepository.save(entity);

        } catch (Exception e) {
            System.out.println("===== [AI BATTERY ERROR OCCURRED] =====");
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}