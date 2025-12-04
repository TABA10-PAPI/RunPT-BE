package com.runpt.back.home.service.implement;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.runpt.back.global.dto.ResponseDto;
import com.runpt.back.home.dto.request.HomeRequestDto;
import com.runpt.back.home.dto.response.HomeRecommendationDto;
import com.runpt.back.home.dto.response.HomeResponseDto;
import com.runpt.back.home.service.HomeService;
import com.runpt.back.user.entity.BatteryEntity;
import com.runpt.back.user.entity.RunningSessionEntity;
import com.runpt.back.user.entity.UserEntity;
import com.runpt.back.user.repository.BatteryRepository;
import com.runpt.back.user.repository.RunningSessionRepository;
import com.runpt.back.user.repository.TierRepository;
import com.runpt.back.user.repository.UserRepository;
import com.runpt.back.user.util.TierCalculator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeServiceImplement implements HomeService {

    private final BatteryRepository batteryRepository;
    private final UserRepository userRepository;
    private final TierRepository tierRepository;
    private final RunningSessionRepository runningSessionRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ResponseEntity<? super HomeResponseDto> getHome(HomeRequestDto dto) {
        int distance = 0;
        Long uid = dto.getUid();
        String date = dto.getDate();
        String nickname = null;
        float batteryvalue = 0;
        HomeRecommendationDto firstRecommendation = null;
        String tier = null;

        try {

            if (uid == null) return HomeResponseDto.uidNotExists();
            if (date == null || date.isBlank()) return HomeResponseDto.dateNotExists();

            // 1) user table → nickname
            UserEntity user = userRepository.findById(uid).orElse(null);
            if (user == null) return HomeResponseDto.userNotExists();

            nickname = user.getNickname();

            // 2) battery table → batteryvalue + recommendationsJson
            BatteryEntity battery = batteryRepository.findByUidAndDate(uid, date)
                    .orElse(null);
            if (battery == null) return HomeResponseDto.batteryNotFound();

            batteryvalue = battery.getBattery();

            // recommendationsJson → 첫 번째 추천만 꺼내기
            

            if (battery.getRecommendationsJson() != null) {
                List<HomeRecommendationDto> recommendations;
                try {
                    recommendations = objectMapper.readValue(
                            battery.getRecommendationsJson(),
                            new TypeReference<List<HomeRecommendationDto>>() {}
                    );
                    if (!recommendations.isEmpty()) {
                        firstRecommendation = recommendations.get(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return HomeResponseDto.recommendationParamError();
                }
            }

            // 3) runningsession → 최근 distance
            distance = runningSessionRepository.findTop1ByUidOrderByDateDesc(uid)
                .map(RunningSessionEntity::getDistance)
                .orElse(0);
            // 4) tierrecord → 모든 카테고리 중 가장 높은 티어
            tier = tierRepository.findOneByUid(uid)
                    .map(TierCalculator::getHighestTierFromEntity)
                    .orElse("UNRANKED");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return HomeResponseDto.success(uid, date, nickname, batteryvalue, firstRecommendation, distance, tier);
    }
}
