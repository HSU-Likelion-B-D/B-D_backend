package com.likelion.bd.domain.campaign.web.dto;

import java.util.Optional;

public record PaymentListRes(
        Long paymentId,
        String imgUrl,
        String title, // 제안서 제목
        String offerBudget, // 요청 금액
        int fee, // 수수료
        Long totalPaid, // 실 납부금액
        String startDate, // 시작 날짜
        String endDate, // 종료 날짜
        String status, // 상태
        Long reviewedId,

        Boolean tf, // 자영업자일 때, 리뷰 작성했는지 안했는지

        // 내가 인플루언서일 때, 상대 자영업자 정보, 있을 수도 없을 수도
        Optional<PaymentListRes.reviewInfo> reviewInfo
) {

    public record reviewInfo(
            String workplaceName,
            String introduction
    ) {}
}
