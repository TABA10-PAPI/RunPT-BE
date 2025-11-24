package com.runpt.back.home.service.implement;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.runpt.back.ai.entity.BatteryEntity;
import com.runpt.back.ai.repository.BatteryRepository;
import com.runpt.back.global.dto.ResponseDto;
import com.runpt.back.home.dto.request.HomeRequestDto;
import com.runpt.back.home.dto.response.HomeRecommendationDto;
import com.runpt.back.home.dto.response.HomeResponseDto;
import com.runpt.back.home.service.HomeService;
//import com.runpt.back.runningSession.entity.RunningSessionEntity;
//import com.runpt.back.runningSession.repository.RunningSessionRepository;
//import com.runpt.back.tier.entity.TierRecordEntity;
//import com.runpt.back.tier.repository.TierRecordRepository;
//import com.runpt.back.user.entity.UserEntity;
//import com.runpt.back.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeServiceImplement implements HomeService {

    private final BatteryRepository batteryRepository;
    //private final UserRepository userRepository;
    //private final TierRecordRepository tierRecordRepository;
    //private final RunningSessionRepository runningSessionRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ResponseEntity<? super HomeResponseDto> getHome(HomeRequestDto dto) {

        try {
            Long uid = dto.getUid();
            String date = dto.getDate();

            // 1) user table → nickname
            String nickname = "june" ;/*userRepository.findById(uid)
                    .map(UserEntity::getNickname)
                    .orElse("Unknown");*/

            // 2) battery table → batteryvalue + recommendationsJson
            BatteryEntity battery = batteryRepository.findByUidAndDate(uid, date)
                    .orElseThrow(() -> new RuntimeException("No battery data"));

            float batteryvalue = battery.getBattery();

            // recommendationsJson → 첫 번째 추천만 꺼내기
            HomeRecommendationDto firstRecommendation = null;

            if (battery.getRecommendationsJson() != null) {
                List<HomeRecommendationDto> list = objectMapper.readValue(
                        battery.getRecommendationsJson(),
                        new TypeReference<List<HomeRecommendationDto>>() {}
                );

                if (!list.isEmpty()) firstRecommendation = list.get(0);
            }

            // 3) runningsession → 최근 distance
            int distance =  3;/*runningSessionRepository.findTopByUidOrderByDateDesc(uid)
                    .map(RunningSessionEntity::getDistance)
                    .orElse(0);*/

            // 4) tierrecord → 최신 tier
            String tier = "gold"; /*tierRecordRepository.findTopByUidOrderByCreatedAtDesc(uid)
                    .map(TierRecordEntity::getTier)
                    .orElse("UNRANKED");*/

            return HomeResponseDto.success(
                    uid,
                    date,
                    nickname,
                    batteryvalue,
                    firstRecommendation,
                    distance,
                    tier
            );

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
    }
}
