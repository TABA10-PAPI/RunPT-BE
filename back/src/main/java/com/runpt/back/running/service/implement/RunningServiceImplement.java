package com.runpt.back.running.service.implement;

import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.runpt.back.ai.entity.BatteryEntity;
import com.runpt.back.ai.repository.BatteryRepository;
import com.runpt.back.global.dto.ResponseDto;
import com.runpt.back.running.dto.request.RunningRequestDto;
import com.runpt.back.running.dto.response.RunningRecommendationDto;
import com.runpt.back.running.dto.response.RunningResponseDto;
import com.runpt.back.running.service.RunningService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RunningServiceImplement implements RunningService {

    private final BatteryRepository batteryRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ResponseEntity<? super RunningResponseDto> getRunningPlan(RunningRequestDto dto) {
        try {
            Long uid = dto.getUid();
            String date = dto.getDate();
            
            if (uid == null) throw new RuntimeException("요청 uid가 null 입니다");
            if (date == null || date.isBlank()) throw new RuntimeException("요청 date가 비어있습니다");

            
            BatteryEntity battery = batteryRepository.findByUidAndDate(uid, date)
                    .orElseThrow(() -> new RuntimeException(
                            "Battery 데이터 없음 → uid=" + uid + ", date=" + date));

            String json = battery.getRecommendationsJson();

            List<RunningRecommendationDto> recommendations;

            if (json == null || json.isBlank()) {
                
                recommendations = Collections.emptyList();
            } else {
                try {
                    recommendations = objectMapper.readValue(
                            json,
                            new TypeReference<List<RunningRecommendationDto>>() {}
                    );
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    recommendations = Collections.emptyList();  // 파싱 실패 시 안전 처리
                }
            }

            return RunningResponseDto.success(uid, date, recommendations);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
    }
}

