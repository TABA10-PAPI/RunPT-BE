package com.runpt.back.home.service.implement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.runpt.back.global.dto.ResponseDto;
import com.runpt.back.home.dto.request.HomeRequestDto;
import com.runpt.back.home.dto.response.HomeResponseDto;
import com.runpt.back.home.service.HomeService;
import com.runpt.back.home.dto.request.BatteryToAiRequestDto;
import com.runpt.back.user.entity.BatteryEntity;
import com.runpt.back.user.entity.RunningSessionEntity;
import com.runpt.back.user.entity.TierEntity;
import com.runpt.back.user.entity.UserEntity;
import com.runpt.back.user.repository.BatteryRepository;
import com.runpt.back.user.repository.RunningSessionRepository;
import com.runpt.back.user.repository.TierRepository;
import com.runpt.back.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeServiceImplement implements HomeService {

    private final BatteryRepository batteryRepository;
    private final UserRepository userRepository;
    private final TierRepository tierRepository;
    private final RunningSessionRepository runningSessionRepository;

    @Override
    public ResponseEntity<? super HomeResponseDto> getHome(HomeRequestDto dto) {
        Long uid = dto.getUid();
        String date = dto.getDate();
        String nickname = null;
        BatteryEntity battery = null;
        TierEntity tier = null;
        List<RunningSessionEntity> last7daysRuns = null;
        
        try {

            if (uid == null) return HomeResponseDto.uidNotExists();
            if (date == null || date.isBlank()) return HomeResponseDto.dateNotExists();

            // 1) user table → nickname
            UserEntity user = userRepository.findById(uid).orElse(null);
            if (user == null) return HomeResponseDto.userNotExists();
            tier = tierRepository.findByUser_Id(uid);

            nickname = user.getNickname();

            //battter정보 최신화
            getBatteryInfo(uid, date);

            // 2) battery table → batteryvalue + recommendationsJson
            battery = batteryRepository.findByUser_Id(uid);


            if (battery == null) return HomeResponseDto.batteryNotFound();

            //최근 7일간 running session 정보 가져오기
            LocalDate targetDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            LocalDateTime end = targetDate.atTime(23, 59, 59);
            LocalDateTime start = targetDate.minusDays(6).atStartOfDay(); // 날짜 포함 7일간

            last7daysRuns = runningSessionRepository
                    .findByUser_IdAndDateBetweenOrderByDateDesc(uid, start, end);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return HomeResponseDto.success(uid, date, nickname, battery, tier, last7daysRuns);
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
}
