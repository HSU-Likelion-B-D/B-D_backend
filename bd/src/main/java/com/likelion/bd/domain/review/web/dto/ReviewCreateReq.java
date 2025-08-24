package com.likelion.bd.domain.review.web.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewCreateReq {

    @NotNull(message = "리뷰 대상 ID는 필수 입력 값 입니다.")
    private Long reviewedId;

    @NotNull(message = "평점은 필수 입력 값 입니다.")
    @DecimalMin(value = "0.0", message = "평점은 0점 이상이어야 합니다.")
    @DecimalMax(value = "5.0", message = "평점은 5점 이하여야 합니다.")
    private Double score;

    @NotBlank(message = "리뷰 내용은 필수 입력 값 입니다.")
    private String content; // 리뷰 내용

    private Long paymentId; // 자영업자 기준
}
