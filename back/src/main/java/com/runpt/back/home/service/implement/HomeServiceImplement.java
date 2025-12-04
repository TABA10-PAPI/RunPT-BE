package com.runpt.back.home.service.implement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.runpt.back.global.dto.ResponseDto;
import com.runpt.back.home.dto.request.HomeRequestDto;
import com.runpt.back.home.dto.response.HomeResponseDto;
import com.runpt.back.home.service.HomeService;
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
            tier = tierRepository.findByUid(uid);

            nickname = user.getNickname();

            // 2) battery table → batteryvalue + recommendationsJson
            battery = batteryRepository.findByUid(uid);

            if (battery == null) return HomeResponseDto.batteryNotFound();

            //최근 7일간 running session 정보 가져오기
            LocalDate targetDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            LocalDateTime end = targetDate.atTime(23, 59, 59);
            LocalDateTime start = targetDate.minusDays(6).atStartOfDay(); // 날짜 포함 7일간

            last7daysRuns = runningSessionRepository
                    .findByUidAndDateBetweenOrderByDateDesc(uid, start, end);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return HomeResponseDto.success(uid, date, nickname, battery, tier, last7daysRuns);
    }
}
