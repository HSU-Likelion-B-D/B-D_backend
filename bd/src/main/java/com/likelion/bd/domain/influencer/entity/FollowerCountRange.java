package com.likelion.bd.domain.influencer.entity;

public enum FollowerCountRange {

    LESS_THAN_1K, // 1천 미만
    BETWEEN_1K_AND_5K, // 1천 ~ 5천
    BETWEEN_5K_AND_10K, // 5천 ~ 1만
    BETWEEN_10K_AND_100K, // 1만 ~ 10만
    OVER_100K; // 10만 이상

    public static FollowerCountRange fromValue(int value) {
        int index = value - 1;
        FollowerCountRange[] ranges = values();

        if (index >= 0 && index < ranges.length) {
            return ranges[index];
        }
        throw new IllegalArgumentException("유효하지 않은 팔로워 수 범위 값입니다: " + value);
    }
}
