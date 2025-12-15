package com.runpt.back.user.service.implement;

import java.util.List;
import java.util.Optional;
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
import com.runpt.back.user.dto.request.AddUserRequestDto;
import com.runpt.back.user.dto.request.BatteryToAiRequestDto;
import com.runpt.back.user.dto.request.GetMyPageRequestDto;
import com.runpt.back.user.dto.request.JoinRequestDto;
import com.runpt.back.user.dto.request.KakaoLoginRequestDto;
import com.runpt.back.user.dto.request.NaverLoginRequestDto;
import com.runpt.back.user.dto.request.SaveRunningRequestDto;
import com.runpt.back.user.dto.request.RunningToAIRequestDto;

import com.runpt.back.user.dto.response.AddUserResponseDto;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
            if (dto.getAccessToken() == null || dto.getAccessToken().trim().isEmpty()) {
                return KakaoLoginResponseDto.invalidAccessToken();
            }

            KakaoUserInfo info = kakaoOauthHelper.getUserInfoFromToken(dto.getAccessToken());

            if (info == null) return KakaoLoginResponseDto.oauthApiError();


            String kakaoId = info.getId();
            if(kakaoId == null || kakaoId.trim().isEmpty()) {
                return KakaoLoginResponseDto.oauthApiError();
            }

            UserEntity user = userRepository.findByOauthProviderAndOauthUid("kakao", kakaoId);

            if (user == null) {
                fresh = true;
                user = new UserEntity();
                user.setOauthProvider("kakao");
                user.setOauthUid(kakaoId);
                user.setNickname(info.getNickname() != null ? info.getNickname() : "닉네임 없음");
                userRepository.save(user);
            }

            nickname = user.getNickname();
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
            if (dto.getAccessToken() == null || dto.getAccessToken().trim().isEmpty()) {
                return NaverLoginResponseDto.invalidAccessToken();
            }

            NaverUserInfo info = naverOauthHelper.getUserInfoFromToken(dto.getAccessToken());

            if (info == null) return NaverLoginResponseDto.oauthApiError();

            String naverId = info.getId();
            if(naverId == null || naverId.trim().isEmpty()) {
                return KakaoLoginResponseDto.oauthApiError();
            }

            UserEntity user = userRepository.findByOauthProviderAndOauthUid("naver", naverId);

            if (user == null) {
                fresh = true;
                user = new UserEntity();
                user.setOauthProvider("naver");
                user.setOauthUid(naverId);
                user.setNickname(nickname != null ? info.getNickname() : "닉네임 없음");
                userRepository.save(user);
            }
            
            nickname = user.getNickname();
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

            if (dto.getUid() <= 0) return JoinResponseDto.validationFail();
            if (dto.getAge() <= 0 || dto.getAge() > 150) return JoinResponseDto.validationFail();
            if (dto.getHeight() <= 0 || dto.getHeight() > 250) return JoinResponseDto.validationFail();
            if (dto.getWeight() <= 0 || dto.getWeight() > 230) return JoinResponseDto.validationFail();
            if (!"M".equalsIgnoreCase(dto.getGender()) && !"F".equalsIgnoreCase(dto.getGender()))
                return JoinResponseDto.validationFail();
            if (dto.getNickname() == null || dto.getNickname().trim().isEmpty())
                return JoinResponseDto.validationFail();
            
            user.setNickname(dto.getNickname());
            user.setAge(dto.getAge());
            user.setGender(dto.getGender());
            user.setHeight(dto.getHeight());
            user.setWeight(dto.getWeight());
            userRepository.save(user);

            TierEntity tier = new TierEntity(user);
            tierRepository.save(tier);

            return JoinResponseDto.joinSuccess(user);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
    }


    @Override
    public ResponseEntity<? super GetMyPageResponseDto> getMyPage(GetMyPageRequestDto dto) {
        UserEntity user;
        TierEntity tier;
        List<RunningSessionEntity> recent;
        try {
            user = userRepository.findById(dto.getUid());
            if (user == null) return GetMyPageResponseDto.userNotExists();

            tier = tierRepository.findByUser_Id(dto.getUid());  // 티어가 없을 수도 있음
            List<RunningSessionEntity> list =
                    runningSessionRepository.findByUser_IdOrderByDateDesc(dto.getUid());

            recent = list.stream()
                    .limit(5)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return GetMyPageResponseDto.getMyPageSuccess(user, tier, recent);

    }


    @Override
    public ResponseEntity<? super SaveRunningResponseDto> saveRunning(SaveRunningRequestDto dto) {

        log.info("[SAVE RUNNING REQUEST] uid={}, date={}, pace={}, distance={}, durationSec={}, heartRateAvg={}",
        dto.getUid(),
        dto.getDate(),
        dto.getPace(),
        dto.getDistance(),
        dto.getDurationSec(),
        dto.getHeartRateAvg()
        );

        try {
            if (dto.getUid() <= 0) return SaveRunningResponseDto.invalidRunningData();
            if (dto.getDate() == null) return SaveRunningResponseDto.invalidDateFormat();
            if (dto.getPace() <= 0 ||
                dto.getDistance() <= 0 ||
                dto.getDurationSec() <= 0 ||
                dto.getHeartRateAvg() < 0) {
                return SaveRunningResponseDto.invalidRunningData();
            }

            UserEntity user = userRepository.findById(dto.getUid());
            if (user == null) return SaveRunningResponseDto.userNotExists();
            
            // 0) 중복 체크 - 같은 uid와 date로 이미 저장된 기록이 있는지 확인
            Optional<RunningSessionEntity> existingSession = 
                    runningSessionRepository.findByUser_IdAndDate(dto.getUid(), dto.getDate());
            if (existingSession.isPresent()) {
                return SaveRunningResponseDto.runningSaveFailed(); // 또는 중복 에러 메시지
            }

            // 1) 러닝 기록 저장
            try {
                RunningSessionEntity session = new RunningSessionEntity();
                session.setUser(user);
                session.setDistance(dto.getDistance());
                session.setDate(dto.getDate());
                session.setDurationSec(dto.getDurationSec());
                session.setHeartRateAvg(dto.getHeartRateAvg());
                session.setPace(dto.getPace());
                runningSessionRepository.save(session);

            } catch (Exception e) {
                e.printStackTrace();
                return SaveRunningResponseDto.runningSaveFailed();
            }

            // 2) 티어 엔티티 조회
            TierEntity tier = tierRepository.findByUser_Id(dto.getUid());
            if (tier == null) {
                tier = new TierEntity(user);
            }

            double dis = dto.getDistance();
        

            String category;
            if (dis >= 3000 && dis < 5000)          category = "KM3";
            else if (dis >= 5000 && dis < 10000)    category = "KM5";
            else if (dis >= 10000 && dis < 21097.5)   category = "KM10";
            else if (dis >= 21097.5 && dis < 42195)   category = "HALF";
            else if (dis >= 42195)                      category = "FULL";
            else if (dis < 3000)              category = "JOGGING";
            else {
                return SaveRunningResponseDto.invalidRunningData(); 
            }

            boolean isShort = category.equals("KM3") || category.equals("KM5") || category.equals("KM10");

            // 3) 이번 러닝의 티어 계산 = 초 단위 페이스
            TierCalculator.Tier newTier;
            try {
                newTier = isShort
                        ? TierCalculator.calculateShortDistanceTier(dto.getPace())
                        : TierCalculator.calculateLongDistanceTier(dto.getPace());
            } catch (Exception e) {
                return SaveRunningResponseDto.tierCalculationError();  
            }

            // 4) 기존 티어와 비교하여 더 나은 티어면 업데이트
            try {
                String oldRank = switch (category) {
                    case "KM3" -> tier.getKm3();
                    case "KM5" -> tier.getKm5();
                    case "KM10" -> tier.getKm10();
                    case "HALF" -> tier.getHalf();
                    case "FULL" -> tier.getFull();
                    default -> null;
                };

                boolean isBetter = (oldRank == null) ||
                        (TierCalculator.getTierPriority(newTier) >
                         TierCalculator.getTierPriority(TierCalculator.Tier.valueOf(oldRank)));

                if (isBetter) {
                    switch (category) {
                        case "KM3" -> tier.setKm3(newTier.name());
                        case "KM5" -> tier.setKm5(newTier.name());
                        case "KM10" -> tier.setKm10(newTier.name());
                        case "HALF" -> tier.setHalf(newTier.name());
                        case "FULL" -> tier.setFull(newTier.name());
                    }
                }
            
                tierRepository.save(tier);
            } catch (Exception e) {
                e.printStackTrace();
                return SaveRunningResponseDto.tierSaveFailed();
            }

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
                dto.getUid(),                     // user_id
                dto.getDate().toString(),         // date
                dto.getDistance(),                // distance
                dto.getPace(),                    // pace_sec  (String -> int 변경)
                dto.getDurationSec(),             // time_sec
                dto.getHeartRateAvg()             // avg_hr
            );

            HttpEntity<RunningToAIRequestDto> entity = new HttpEntity<>(aiDto, headers);
            RestTemplate rt = new RestTemplate();

            ResponseEntity<String> response = rt.postForEntity(url, entity, String.class);

            System.out.println("[AI RUNNING SEND] STATUS → " + response.getStatusCode());
            System.out.println("[AI RUNNING SEND] RESPONSE → " + response.getBody());

        } catch (Exception e) {
            System.out.println("[AI RUNNING SEND] ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void getBatteryInfo(Long uid, String date) {
        System.out.println("===== [AI BATTERY REQUEST START] =====");
        System.out.println("UID = " + uid);
        System.out.println("DATE = " + date);

        try {
            RestTemplate rt = new RestTemplate();

            // -----------------------------------
            // 1) 배터리 점수 요청 (/battery/score)
            // -----------------------------------
            String scoreUrl = "http://13.124.197.160:8000/battery/score";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            BatteryToAiRequestDto dto = new BatteryToAiRequestDto(uid, date);
            HttpEntity<BatteryToAiRequestDto> req = new HttpEntity<>(dto, headers);

            JsonNode scoreRes = rt.postForObject(scoreUrl, req, JsonNode.class);

            System.out.println("---- AI SCORE RESPONSE ----");
            System.out.println(scoreRes == null ? "NULL" : scoreRes.toPrettyString());

            if (scoreRes == null || !scoreRes.has("battery_score")) {
                throw new RuntimeException("AI 점수 응답이 잘못되었습니다.");
            }

            float battery = (float) scoreRes.get("battery_score").asDouble();
            String feedback = scoreRes.has("feedback") ? scoreRes.get("feedback").asText() : null;
            String reason = scoreRes.has("reason") ? scoreRes.get("reason").asText() : null;


            // -----------------------------------
            // 2) 추천 요청 (/battery/recommendation)
            // -----------------------------------
            String recUrl = "http://13.124.197.160:8000/battery/recommendations";

            JsonNode recRes = rt.postForObject(recUrl, req, JsonNode.class);

            System.out.println("---- AI RECOMMENDATION RESPONSE ----");
            System.out.println(recRes == null ? "NULL" : recRes.toPrettyString());

            if (recRes == null || !recRes.has("recommendations")) {
                throw new RuntimeException("AI 추천 응답이 잘못되었습니다.");
            }

            String recommendationsJson = recRes.get("recommendations").toString();


            // -----------------------------------
            // 3) DB UPSERT
            // -----------------------------------
            BatteryEntity batteryEntity = batteryRepository.findByUser_Id(uid);

            UserEntity user = userRepository.findById(uid)
                    .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

            if (batteryEntity == null) {
                batteryEntity = new BatteryEntity();
                batteryEntity.setUser(user);
            }else {
                batteryRepository.delete(batteryEntity);
                batteryEntity = new BatteryEntity();
                batteryEntity.setUser(user);
            }

            batteryEntity.setDate(date);
            batteryEntity.setBattery(battery);
            batteryEntity.setFeedback(feedback);
            batteryEntity.setReason(reason);
            batteryEntity.setRecommendationsJson(recommendationsJson);

            batteryRepository.save(batteryEntity);

        } catch (Exception e) {
            System.out.println("===== [AI BATTERY ERROR OCCURRED] =====");
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public ResponseEntity<? super AddUserResponseDto> addUser(AddUserRequestDto dto) {
        try {
            UserEntity user = userRepository.findById(dto.getUid())
                    .orElse(null);
            
            if (user == null) {
                return AddUserResponseDto.userNotExists();
            }

            // DB에 사용자 정보 저장 (업데이트)
            user.setAge(dto.getAge());
            user.setHeight(dto.getHeight());
            user.setWeight(dto.getWeight());
            userRepository.save(user);

            // AI로 사용자 정보 전송
            sendUserToAI(dto.getUid(), dto.getAge(), dto.getHeight(), dto.getWeight());

            return AddUserResponseDto.success();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
    }

    private void sendUserToAI(Long uid, Integer age, Integer height, Integer weight) {
        try {
            String url = "http://13.124.197.160:8000/add-user";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            java.util.Map<String, Object> aiRequest = new java.util.HashMap<>();
            aiRequest.put("user_id", uid.intValue());
            aiRequest.put("age", age);
            aiRequest.put("height", height);
            aiRequest.put("weight", weight);

            HttpEntity<java.util.Map<String, Object>> entity = new HttpEntity<>(aiRequest, headers);
            RestTemplate rt = new RestTemplate();

            ResponseEntity<String> response = rt.postForEntity(url, entity, String.class);

            System.out.println("[AI ADD USER SEND] STATUS → " + response.getStatusCode());
            System.out.println("[AI ADD USER SEND] RESPONSE → " + response.getBody());

        } catch (Exception e) {
            System.out.println("[AI ADD USER SEND] ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
    