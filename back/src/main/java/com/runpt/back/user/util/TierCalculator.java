package com.runpt.back.user.util;

import java.util.stream.Stream;

import com.runpt.back.user.entity.TierEntity;
import lombok.Getter;

public class TierCalculator {

    public enum Tier {
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
            case BRONZE -> 1;
            case SILVER -> 2;
            case GOLD -> 3;
            case PLATINUM -> 4;
            case DIAMOND -> 5;
            case MASTER -> 6;
            case CHALLENGER -> 7;
        };
    }

    public static Tier getHighestTier(Tier... tiers) {
        Tier highest = Tier.BRONZE;
        for (Tier t : tiers)
            if (getTierPriority(t) > getTierPriority(highest))
                highest = t;
        return highest;
    }

    // 각 카테고리(3km, 5km, 10km, Half, Full) 중 가장 높은 티어 반환 
    public static String getHighestTierFromEntity(TierEntity entity) {
        if (entity == null) return "UNRANKED";
        Tier[] tiers = Stream.of(entity.getKm3(), entity.getKm5(), entity.getKm10(), entity.getHalf(), entity.getFull())
                .filter(s -> s != null)
                .map(Tier::valueOf)
                .toArray(Tier[]::new);
        return tiers.length == 0 ? "UNRANKED" : getHighestTier(tiers).name();
    }
}