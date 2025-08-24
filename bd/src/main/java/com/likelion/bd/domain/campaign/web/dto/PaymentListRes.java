package com.likelion.bd.domain.campaign.web.dto;

import java.util.List;
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

        // 내가 인플루언서일 때, 상대 자영업자 정보 / 내가 자영업자 때, 상대 인플루언서 정보
        Optional<PaymentListRes.reviewInfo> reviewInfo
) {

    public interface reviewInfo {}

    public record BusinessManInfo(
            String workplaceName,
            String introduction
    ) implements PaymentListRes.reviewInfo {}

    public record InfluencerInfo(
            String nickname,
            String activityName
    ) implements PaymentListRes.reviewInfo {}
}
