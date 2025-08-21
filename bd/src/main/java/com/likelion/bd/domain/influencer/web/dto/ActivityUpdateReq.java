package com.likelion.bd.domain.influencer.web.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ActivityUpdateReq {

    private String activityName; // 활동명

    @URL(message = "유효한 URL 형식이 아닙니다.")
    private String snsUrl; // sns 주소

    Long followerCount; // 팔로워 수

    @Min(value = 1, message = "주 업로드 횟수는 1에서 5 사이의 값이어야 합니다.")
    @Max(value = 5, message = "주 업로드 횟수는 1에서 5 사이의 값이어야 합니다.")
    Integer uploadFrequency; // 주 업로드 횟수

    private String bankName; // 은행명
    private String accountNumber; // 계좌 번호

    @PositiveOrZero(message = "최소 희망 금액은 0 이상이어야 합니다.")
    private String minBudget; // 최소 희망 금액

    @PositiveOrZero(message = "최대 희망 금액은 0 이상이어야 합니다.")
    private String maxBudget; // 최대 희망 금액

    private List<Long> platformIds; // 활동 플랫폼 카테고리
    private List<Long> contentTopicIds; // 콘텐츠 분야
    private List<Long> contentStyleIds; // 콘텐츠 스타일
    private List<Long> preferTopicIds; // 선호 분야
}
