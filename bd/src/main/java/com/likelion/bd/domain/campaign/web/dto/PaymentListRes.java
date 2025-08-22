package com.likelion.bd.domain.campaign.web.dto;

public record PaymentListRes(
        Long paymentId,
        String title, // 제안서 제목
        String offerBudget, // 요청 금액
        int fee, // 수수료
        Long totalPaid, // 실 납부금액
        String status // 상태
) {
}
