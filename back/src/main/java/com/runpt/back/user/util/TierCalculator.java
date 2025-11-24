package com.runpt.back.user.util;

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

    /**
     * 3KM, 5KM, 10KM 거리의 티어 계산
     * 브론즈(9~8분), 실버(8~7분), 골드(7~6분), 플레티넘(6~5분), 
     * 다이아몬드(5~4분30초), 마스터(4분30초~4분), 챌린저(4분 언더)
     * @param pace 분/km 단위
     * @return 티어
     */
    public static Tier calculateShortDistanceTier(int pace) {
        if (pace <= 0) {
            return Tier.BRONZE;
        }

        if (pace >= 9.0) {
            return Tier.BRONZE;
        } else if (pace >= 8.0) {  // 8.0 <= pace < 9.0
            return Tier.SILVER;
        } else if (pace >= 7.0) {  // 7.0 <= pace < 8.0
            return Tier.GOLD;
        } else if (pace >= 6.0) {  // 6.0 <= pace < 7.0
            return Tier.PLATINUM;
        } else if (pace >= 5.0) {  // 5.0 <= pace < 6.0
            return Tier.DIAMOND;
        } else if (pace >= 4.5) {  // 4.5 <= pace < 5.0 (4분30초~5분)
            return Tier.MASTER;
        } else {  // pace < 4.5 (4분 언더)
            return Tier.CHALLENGER;
        }
    }

    /**
     * 하프마라톤, 풀마라톤 거리의 티어 계산
     * 브론즈(9~8분), 실버(8~7분), 골드(7~6분), 플레티넘(6~5분30초),
     * 다이아몬드(5분30초~5분), 마스터(5~4분30초), 챌린저(4분30초 언더)
     * @param pace 분/km 단위
     * @return 티어
     */
    public static Tier calculateLongDistanceTier(int pace) {
        if (pace <= 0) {
            return Tier.BRONZE;
        }

        if (pace >= 9.0) {
            return Tier.BRONZE;
        } else if (pace >= 8.0) {  // 8.0 <= pace < 9.0
            return Tier.SILVER;
        } else if (pace >= 7.0) {  // 7.0 <= pace < 8.0
            return Tier.GOLD;
        } else if (pace >= 6.0) {  // 6.0 <= pace < 7.0
            return Tier.PLATINUM;
        } else if (pace >= 5.5) {  // 5.5 <= pace < 6.0 (5분30초~6분)
            return Tier.DIAMOND;
        } else if (pace >= 4.5) {  // 4.5 <= pace < 5.5 (4분30초~5분30초, 즉 5분~4분30초)
            return Tier.MASTER;
        } else {  // pace < 4.5 (4분30초 언더)
            return Tier.CHALLENGER;
        }
    }

    /**
     * 거리 타입에 따라 적절한 티어 계산 메서드 호출
     * @param distanceType "3KM", "5KM", "10KM", "HALF", "FULL"
     * @param pace 분/km 단위
     * @return 티어
     */
    public static Tier calculateTier(String distanceType, int pace) {
        if (distanceType == null) {
            return Tier.BRONZE;
        }

        if ("HALF".equals(distanceType) || "FULL".equals(distanceType)) {
            return calculateLongDistanceTier(pace);
        } else {
            return calculateShortDistanceTier(pace);
        }
    }

    /**
     * 티어 우선순위 비교 (숫자로 변환)
     * @param tier 티어
     * @return 우선순위 (높을수록 좋은 티어)
     */
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

    /**
     * 여러 티어 중 가장 높은 티어 반환
     * @param tiers 티어 배열
     * @return 가장 높은 티어
     */
    public static Tier getHighestTier(Tier... tiers) {
        Tier highest = Tier.BRONZE;
        for (Tier tier : tiers) {
            if (getTierPriority(tier) > getTierPriority(highest)) {
                highest = tier;
            }
        }
        return highest;
    }
}

