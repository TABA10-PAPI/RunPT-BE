// ----------------------
// 수정된 부분 포함 완료
// stress 필드 optional 처리
// ----------------------

package com.runpt.back.ai.service.implement;

import java.util.List;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.runpt.back.ai.dto.request.AiBatteryRequestDto;
import com.runpt.back.ai.entity.BatteryEntity;
import com.runpt.back.ai.repository.BatteryRepository;
import com.runpt.back.ai.service.AiService;

import com.runpt.back.ai.dto.response.AiBatteryResponseDto;
import com.runpt.back.ai.dto.response.AiBatteryResponseDto.RecommendationDto;

import com.runpt.back.global.dto.ResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiServiceImplement implements AiService {

    private static final String EXTERNAL_AI_URL = "http://13.124.197.160:8000/battery";

    private final RestTemplate restTemplate;
    private final BatteryRepository batteryRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ResponseEntity<? super AiBatteryResponseDto> analyzeBattery(AiBatteryRequestDto dto) {

        System.out.println("===== [AI BATTERY REQUEST START] =====");
        System.out.println("UID = " + dto.getUid());
        System.out.println("DATE = " + dto.getDate());

        float battery = 0;
        List<RecommendationDto> recList = null;

        try {
            Long uid = dto.getUid();
            String date = dto.getDate();

            // ---------------------------
            // 1) 외부 AI 통신
            // ---------------------------
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<AiBatteryRequestDto> req = new HttpEntity<>(dto, headers);

            JsonNode aiRes = restTemplate.postForObject(
                    EXTERNAL_AI_URL,
                    req,
                    JsonNode.class
            );

            System.out.println("---- AI RAW RESPONSE ----");
            System.out.println(aiRes == null ? "NULL RESPONSE" : aiRes.toPrettyString());

            if (aiRes == null) {
                throw new RuntimeException("AI 응답이 NULL입니다.");
            }

            // ---------------------------
            // 2) JSON 파싱
            // ---------------------------

            // battery_score (필수값)
            if (!aiRes.has("battery_score")) {
                throw new RuntimeException("AI 응답에 battery_score 필드가 없습니다.");
            }
            battery = aiRes.get("battery_score").floatValue();

            // recommendations (필수 배열)
            JsonNode recNode = aiRes.get("recommendations");
            if (recNode == null || !recNode.isArray()) {
                throw new RuntimeException("AI 응답에 recommendations 배열이 없습니다.");
            }

            String recommendationsJson = recNode.toString();


            // ---------------------------
            // 3) DB UPSERT
            // ---------------------------
            BatteryEntity entity = batteryRepository.findByUidAndDate(uid, date)
                    .orElseGet(() -> BatteryEntity.builder()
                            .uid(uid)
                            .date(date)
                            .build()
                    );

            entity.setBattery(battery);
            entity.setRecommendationsJson(recommendationsJson);

            batteryRepository.save(entity);

            // ---------------------------
            // 4) BatteryResponseDto 로 변환
            // ---------------------------
            recList =
                    objectMapper.readValue(
                            recommendationsJson,
                            objectMapper.getTypeFactory().constructCollectionType(
                                    List.class, RecommendationDto.class
                            )
                    );

        } catch (Exception e) {

            System.out.println("===== [AI BATTERY ERROR OCCURRED] =====");
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();

            return ResponseDto.databaseError();
        }
        
        return AiBatteryResponseDto.success(battery, recList);
    }
}
