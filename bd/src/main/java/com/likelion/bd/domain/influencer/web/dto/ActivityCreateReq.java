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
public class ActivityCreateReq {

    @NotNull(message = "사용자 ID는 필수 입력 값입니다.")
    private Long userId;

    @NotBlank(message = "활동명은 필수 입력 항목입니다.")
    private String activityName; // 활동명

    @NotBlank(message = "SNS 주소는 필수 입력 항목입니다.")
    @URL(message = "유효한 URL 형식이 아닙니다.")
    private String snsUrl; // sns 주소

    @NotNull(message = "팔로워 수는 필수 입력 값입니다.")
    Long followerCount;

    @NotNull(message = "주 업로드 횟수를 선택해주세요.")
    @Min(value = 1, message = "주 업로드 횟수는 1에서 5 사이의 값이어야 합니다.")
    @Max(value = 5, message = "주 업로드 횟수는 1에서 5 사이의 값이어야 합니다.")
    int uploadFrequency; // 주 업로드 횟수

    @NotBlank(message = "은행명은 필수 입력 항목입니다.")
    private String bankName; // 은행명

    @NotBlank(message = "계좌번호는 필수 입력 항목입니다.")
    private String accountNumber; // 계좌 번호

    @PositiveOrZero(message = "최소 희망 금액은 0 이상이어야 합니다.")
    private Long minAmount; // 최소 희망 금액

    @PositiveOrZero(message = "최대 희망 금액은 0 이상이어야 합니다.")
    private Long maxAmount; // 최대 희망 금액

    @NotEmpty(message = "플랫폼을 하나 이상 선택해주세요.")
    private List<Long> platformIds; // 활동 플랫폼 카테고리

    @NotEmpty(message = "콘텐츠 분야를 하나 이상 선택해주세요.")
    private List<Long> contentTopicIds; // 콘텐츠 분야

    @NotEmpty(message = "콘텐츠 스타일을 하나 이상 선택해주세요.")
    private List<Long> contentStyleIds; // 콘텐츠 스타일

    @NotEmpty(message = "선호 분야를 하나 이상 선택해주세요.")
    private List<Long> preferTopicIds; // 선호 분야
}
