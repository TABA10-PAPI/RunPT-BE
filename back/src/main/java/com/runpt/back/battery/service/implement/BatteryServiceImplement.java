package com.runpt.back.battery.service.implement;

import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.runpt.back.battery.dto.request.BatteryRequestDto;
import com.runpt.back.battery.dto.response.BatteryResponseDto;
import com.runpt.back.battery.dto.response.BatteryResponseDto.RecommendationDto;

import com.runpt.back.ai.entity.BatteryEntity;
import com.runpt.back.ai.repository.BatteryRepository;

import com.runpt.back.battery.service.BatteryService;
import com.runpt.back.global.dto.ResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BatteryServiceImplement implements BatteryService {

    private final BatteryRepository batteryRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    float battery = 0;
    List<RecommendationDto> recommendations = null;
    @Override
    public ResponseEntity<? super BatteryResponseDto> getBattery(BatteryRequestDto dto) {
        try {
            BatteryEntity entity = batteryRepository.findByUidAndDate(dto.getUid(), dto.getDate())
                    .orElseThrow(() -> new RuntimeException("해당 날짜 데이터 없음"));

            // JSON 파싱
            recommendations = Collections.emptyList();
            if (entity.getRecommendationsJson() != null) {
                recommendations = objectMapper.readValue(
                        entity.getRecommendationsJson(),
                        new TypeReference<List<RecommendationDto>>() {}
                );
            }
            
            battery = entity.getBattery();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return BatteryResponseDto.success(battery, recommendations);
    }
}
