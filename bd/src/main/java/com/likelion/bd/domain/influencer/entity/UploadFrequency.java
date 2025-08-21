package com.likelion.bd.domain.influencer.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UploadFrequency {

    LESS_THAN_ONCE(1), // 1회 미만
    ONCE_OR_TWICE(2), // 1회 ~ 2회
    THREE_OR_FOUR_TIMES(3), // 3회 ~ 4회
    FIVE_OR_SIX_TIMES(4), // 5회 ~ 6회
    SEVEN_TIMES(5); // 7회

    private final int value;

    public static UploadFrequency fromValue(int value) {
        for (UploadFrequency frequency : values()) {
            if (frequency.getValue() == value) {
                return frequency;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 업로드 횟수 값입니다: " + value);
    }
}
