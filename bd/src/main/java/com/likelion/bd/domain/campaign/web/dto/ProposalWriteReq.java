package com.likelion.bd.domain.campaign.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProposalWriteReq {

    @NotBlank(message = "제목은 필수 입력 값 입니다.")
    private String title; // 제안서 제목

    @NotNull(message = "제시 금액 필수 입력 값 입니다.")
    private Long offerAmount; // 제시 금액

    @NotBlank(message = "시작 날짜는 필수 입력 값 입니다.")
    private String startDate; // 시작 날짜

    @NotBlank(message = "종료 날짜는 필수 입력 값 입니다.")
    private String endDate; // 종료 날짜

    @NotBlank(message = "개요는 필수 입력 값 입니다.")
    private String overView; // 개요

        private String request; // 요청사항
}
