package com.runpt.back.user.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.runpt.back.user.entity.TierEntity;
import lombok.Getter;

public class TierCalculator {
    public enum Tier {
        UNRANKED("언랭크"),
        BRONZE("브론즈"),
        SILVER("실버"),
        GOLD("골드"),
        PLATINUM("플레티넘"),
        DIAMOND("다이아몬드"),
        MASTER("마스터"),
        CHALLENGER("챌린저");

        @Getter
        private final String koreanName;

        Tier(String koreanName) {
            this.koreanName = koreanName;
        }
    }
    /** 단거리 3/5/10km: pace = 1km당 초 */
    public static Tier calculateShortDistanceTier(int paceSec) {
        if (paceSec <= 0) return Tier.BRONZE;

        if (paceSec >= 540) return Tier.BRONZE;        // 9:00~
        else if (paceSec >= 480) return Tier.SILVER;   // 8:00~
        else if (paceSec >= 420) return Tier.GOLD;     // 7:00~
        else if (paceSec >= 360) return Tier.PLATINUM; // 6:00~
        else if (paceSec >= 300) return Tier.DIAMOND;  // 5:00~
        else if (paceSec >= 240) return Tier.MASTER;   // 4:00~4:30
        else return Tier.CHALLENGER;                   // <4:00
    }

    /** 장거리 하프/풀: pace = 1km당 초 */
    public static Tier calculateLongDistanceTier(int paceSec) {
        if (paceSec <= 0) return Tier.BRONZE;

        if (paceSec >= 540) return Tier.BRONZE;        // 9:00~
        else if (paceSec >= 480) return Tier.SILVER;   // 8:00~
        else if (paceSec >= 420) return Tier.GOLD;     // 7:00~
        else if (paceSec >= 360) return Tier.PLATINUM; // 6:00~
        else if (paceSec >= 330) return Tier.DIAMOND;  // 5:30~
        else if (paceSec >= 270) return Tier.MASTER;   // 4:30~
        else return Tier.CHALLENGER;                   // <4:30
    }

    public static int getTierPriority(Tier tier) {
        return switch (tier) {
            case UNRANKED -> 1;
            case BRONZE -> 2;
            case SILVER -> 3;
            case GOLD -> 4;
            case PLATINUM -> 5;
            case DIAMOND -> 6;
            case MASTER -> 7;
            case CHALLENGER -> 8;
        };
    }

    public static String calculateHighestTier(TierEntity entity, int latestDistance) {
        if (entity == null || latestDistance < 3000) {
            return Tier.UNRANKED.name();
        }

        String[] tierStrings = {
            entity.getKm3(),
            entity.getKm5(),
            entity.getKm10(),
            entity.getHalf(),
            entity.getFull()
        };

        List<Tier> tierList = new ArrayList<>();

        for (String s : tierStrings) {
            if (s != null && !s.isEmpty()) {
                tierList.add(Tier.valueOf(s));  // UNRANKED도 정상 처리됨
            }
        }

        if (tierList.isEmpty()) {
            return Tier.UNRANKED.name();
        }

        Tier highestTier = tierList.stream()
                .max(Comparator.comparing(TierCalculator::getTierPriority))
                .orElse(Tier.UNRANKED);

        return highestTier.name();
    }
}