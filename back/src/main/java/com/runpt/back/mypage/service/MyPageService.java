package com.runpt.back.mypage.service;

import com.runpt.back.mypage.dto.MyPageHomeResponse;
import com.runpt.back.mypage.dto.OverallTierResponse;
import com.runpt.back.mypage.dto.RunningRecordResponse;
import com.runpt.back.profile.entity.Profile;
import com.runpt.back.profile.repository.ProfileRepository;
import com.runpt.back.runningsession.entity.RunningSession;
import com.runpt.back.runningsession.repository.RunningSessionRepository;
import com.runpt.back.tier.entity.TierRecord;
import com.runpt.back.tier.repository.TierRecordRepository;
import com.runpt.back.tier.util.TierCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final ProfileRepository profileRepository;
    private final RunningSessionRepository runningSessionRepository;
    private final TierRecordRepository tierRecordRepository;

    /**
     * 1. 유저의 종합 티어 계산 및 반환
     * TierRecord에서 저장된 티어 정보 조회
     */
    public OverallTierResponse getOverallTier(Long uid) {
        TierRecord tierRecord = tierRecordRepository.findByUserId(uid)
                .orElse(null);

        if (tierRecord == null) {
            return OverallTierResponse.builder()
                    .uid(uid)
                    .highestTier("BRONZE")
                    .highestTierKorean("브론즈")
                    .tierByDistanceType(new HashMap<>())
                    .build();
        }

        // 단거리와 장거리 티어 중 더 높은 것 선택
        TierCalculator.Tier shortTier = tierRecord.getShortTierRank() != null
                ? TierCalculator.Tier.valueOf(tierRecord.getShortTierRank())
                : TierCalculator.Tier.BRONZE;
        TierCalculator.Tier longTier = tierRecord.getLongTierRank() != null
                ? TierCalculator.Tier.valueOf(tierRecord.getLongTierRank())
                : TierCalculator.Tier.BRONZE;

        TierCalculator.Tier highestTier = TierCalculator.getHighestTier(shortTier, longTier);

        // 거리 타입별 티어 정보 구성
        Map<String, String> tierByDistanceType = new HashMap<>();
        if (tierRecord.getShortTierRank() != null) {
            // 단거리 타입별로는 최고 티어를 공통으로 표시 (실제로는 각 거리별로 다를 수 있지만, ERD 구조상 단거리/장거리로만 구분)
            tierByDistanceType.put("SHORT", shortTier.getKoreanName());
        }
        if (tierRecord.getLongTierRank() != null) {
            tierByDistanceType.put("LONG", longTier.getKoreanName());
        }

        return OverallTierResponse.builder()
                .uid(uid)
                .highestTier(highestTier.name())
                .highestTierKorean(highestTier.getKoreanName())
                .tierByDistanceType(tierByDistanceType)
                .build();
    }

    /**
     * 2. 지난 러닝기록 조회
     * RunningSession 기록 기반 티어 계산
     */
    public List<RunningRecordResponse> getRunningHistory(Long uid) {
        List<RunningSession> sessions = runningSessionRepository.findByUserIdOrderByDateDesc(uid);

        return sessions.stream()
                .map(session -> {
                    TierCalculator.Tier tier = TierCalculator.calculateTier(
                            session.getDistanceType(), 
                            session.getPace()
                    );
                    return RunningRecordResponse.from(session, tier.getKoreanName());
                })
                .collect(Collectors.toList());
    }

    /**
     * 3. 마이페이지 홈
     * 프로필정보 + 티어 + 최근기록 일부
     */
    public MyPageHomeResponse getMyPageHome(Long uid) {
        // 프로필 조회
        Profile profile = profileRepository.findByUserId(uid)
                .orElseThrow(() -> new RuntimeException("프로필이 존재하지 않습니다."));

        // 종합 티어 조회
        OverallTierResponse tierResponse = getOverallTier(uid);

        // 최근 기록 일부 (최대 5개)
        List<RunningSession> recentSessions = runningSessionRepository.findRecentRunningSessionsByUserId(
                uid, 
                PageRequest.of(0, 5)
        );

        List<RunningRecordResponse> recentRecords = recentSessions.stream()
                .map(session -> {
                    TierCalculator.Tier tier = TierCalculator.calculateTier(
                            session.getDistanceType(), 
                            session.getPace()
                    );
                    return RunningRecordResponse.from(session, tier.getKoreanName());
                })
                .collect(Collectors.toList());

        return MyPageHomeResponse.builder()
                .uid(profile.getUser().getId())
                .name(profile.getName())
                .age(profile.getAge())
                .height(profile.getHeight())
                .weight(profile.getWeight())
                .region(profile.getRegion())
                .gender(profile.getGender())
                .highestTier(tierResponse.getHighestTier())
                .highestTierKorean(tierResponse.getHighestTierKorean())
                .recentRecords(recentRecords)
                .build();
    }
}

