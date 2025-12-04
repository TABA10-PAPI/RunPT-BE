package com.runpt.back.running.service.implement;

import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.runpt.back.global.dto.ResponseDto;
import com.runpt.back.running.dto.request.*;
import com.runpt.back.running.dto.response.*;
import com.runpt.back.running.dto.response.BatteryResponseDto.RecommendationDto;
import com.runpt.back.running.service.RunningService;
import com.runpt.back.user.entity.BatteryEntity;
import com.runpt.back.user.repository.BatteryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RunningServiceImplement implements RunningService {

    private final BatteryRepository batteryRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ResponseEntity<? super RunningResponseDto> getRunningPlan(RunningRequestDto dto) {
        Long uid = dto.getUid();
        String date = dto.getDate();
        List<RunningRecommendationDto> recommendations = null;

        try {
            
            if (uid == null) return RunningResponseDto.uidNotExists();
            if (date == null || date.isBlank()) return RunningResponseDto.dateNotExists();

            
            BatteryEntity battery = batteryRepository.findByUidAndDate(uid, date)
                    .orElse(null);

            if (battery == null) return RunningResponseDto.batteryNotFound();

            String json = battery.getRecommendationsJson();

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
                    return RunningResponseDto.recommendationParseError();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return RunningResponseDto.success(uid, date, recommendations);
    }

    @Override
    public ResponseEntity<? super BatteryResponseDto> getBattery(BatteryRequestDto dto) {
        float battery = 0;
        List<RecommendationDto> recommendations = null;
        try {
            BatteryEntity entity = batteryRepository.findByUidAndDate(dto.getUid(), dto.getDate())
                    .orElse(null);

            if (entity == null) return BatteryResponseDto.batteryNotFound();

            // JSON 파싱
            recommendations = Collections.emptyList();
            if (entity.getRecommendationsJson() != null) {
                try {
                    recommendations = objectMapper.readValue(
                            entity.getRecommendationsJson(),
                            new TypeReference<List<RecommendationDto>>() {}
                    );
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    return BatteryResponseDto.recommendationParseError();
                }   
            }
            
            battery = entity.getBattery();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return BatteryResponseDto.success(battery, recommendations);
    }
}

