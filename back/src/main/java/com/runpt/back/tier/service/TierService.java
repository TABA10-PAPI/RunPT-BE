package com.runpt.back.tier.service;

import com.runpt.back.runningsession.entity.RunningSession;
import com.runpt.back.runningsession.repository.RunningSessionRepository;
import com.runpt.back.tier.entity.TierRecord;
import com.runpt.back.tier.repository.TierRecordRepository;
import com.runpt.back.tier.util.TierCalculator;
import com.runpt.back.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TierService {

    private final TierRecordRepository tierRecordRepository;
    private final RunningSessionRepository runningSessionRepository;
    private final UserRepository userRepository;

    /**
     * 티어 레코드 업데이트 (러닝 기록 기반으로 재계산)
     */
    @Transactional
    public void updateTierRecord(Long uid) {
        List<RunningSession> sessions = runningSessionRepository.findByUserIdOrderByDateDesc(uid);

        if (sessions.isEmpty()) {
            return;
        }

        // 단거리(3KM, 5KM, 10KM)와 장거리(HALF, FULL) 분리
        Map<String, Double> shortBestPace = new HashMap<>(); // 거리 타입별 최고 페이스
        Map<String, Double> longBestPace = new HashMap<>();
        Map<String, Integer> shortBestTime = new HashMap<>(); // 거리 타입별 최고 시간
        Map<String, Integer> longBestTime = new HashMap<>();

        for (RunningSession session : sessions) {
            String distanceType = session.getDistanceType();
            Double pace = session.getPace();
            Integer duration = session.getDurationSec();

            if (pace == null || duration == null) {
                continue;
            }

            if ("HALF".equals(distanceType) || "FULL".equals(distanceType)) {
                // 장거리
                if (!longBestPace.containsKey(distanceType) || pace < longBestPace.get(distanceType)) {
                    longBestPace.put(distanceType, pace);
                    longBestTime.put(distanceType, duration);
                }
            } else {
                // 단거리
                if (!shortBestPace.containsKey(distanceType) || pace < shortBestPace.get(distanceType)) {
                    shortBestPace.put(distanceType, pace);
                    shortBestTime.put(distanceType, duration);
                }
            }
        }

        // 단거리 최고 티어 계산
        TierCalculator.Tier shortTier = TierCalculator.Tier.BRONZE;
        Integer shortBest = null;
        if (!shortBestPace.isEmpty()) {
            List<TierCalculator.Tier> shortTiers = new ArrayList<>();
            for (Map.Entry<String, Double> entry : shortBestPace.entrySet()) {
                TierCalculator.Tier tier = TierCalculator.calculateShortDistanceTier(entry.getValue());
                shortTiers.add(tier);
            }
            shortTier = TierCalculator.getHighestTier(shortTiers.toArray(new TierCalculator.Tier[0]));

            // 단거리 최고 시간 찾기
            shortBest = shortBestTime.values().stream()
                    .min(Integer::compareTo)
                    .orElse(null);
        }

        // 장거리 최고 티어 계산
        TierCalculator.Tier longTier = TierCalculator.Tier.BRONZE;
        Integer longBest = null;
        if (!longBestPace.isEmpty()) {
            List<TierCalculator.Tier> longTiers = new ArrayList<>();
            for (Map.Entry<String, Double> entry : longBestPace.entrySet()) {
                TierCalculator.Tier tier = TierCalculator.calculateLongDistanceTier(entry.getValue());
                longTiers.add(tier);
            }
            longTier = TierCalculator.getHighestTier(longTiers.toArray(new TierCalculator.Tier[0]));

            // 장거리 최고 시간 찾기
            longBest = longBestTime.values().stream()
                    .min(Integer::compareTo)
                    .orElse(null);
        }

        // TierRecord 저장 또는 업데이트
        TierRecord tierRecord = tierRecordRepository.findByUserId(uid)
                .orElse(TierRecord.builder()
                        .user(userRepository.findById(uid)
                                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다.")))
                        .build());

        tierRecord.setShortTierRank(shortTier.name());
        tierRecord.setLongTierRank(longTier.name());
        tierRecord.setShortBestTime(shortBest);
        tierRecord.setLongBestTime(longBest);

        tierRecordRepository.save(tierRecord);
    }
}

