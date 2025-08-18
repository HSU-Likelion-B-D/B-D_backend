package com.likelion.bd.domain.influencer.entity;

public enum UploadFrequency {

    LESS_THAN_ONCE, // 1회 미만
    ONCE_OR_TWICE, // 1회 ~ 2회
    THREE_OR_FOUR_TIMES, // 3회 ~ 4회
    FIVE_OR_SIX_TIMES, // 5회 ~ 6회
    SEVEN_TIMES; // 7회

    public static UploadFrequency fromValue(int value) {
        int index = value - 1;
        UploadFrequency[] frequencies = values();

        if (index >= 0 && index < frequencies.length) {
            return frequencies[index];
        }
        throw new IllegalArgumentException("유효하지 않은 업로드 횟수 값입니다: " + value);
    }
}
