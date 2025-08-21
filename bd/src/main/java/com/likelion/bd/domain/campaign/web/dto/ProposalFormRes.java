package com.likelion.bd.domain.campaign.web.dto;

import java.util.List;
import java.util.Optional;

public record ProposalFormRes(
        // 기본 정보 필드
        DefaultInfo defaultInfo,
        // 기존 제안서 정보 (Optional), 있을 수도 없을 수도
        Optional<ExistInfo> existInfo
) {

    public interface DefaultInfo {
        String name(); // 실제 이름
        String introduction(); // 자기 소개글
    }

    public record BusinessManInfo(
            String name,
            String introduction,
            String workPlaceName,
            String workPlaceAddress
    ) implements DefaultInfo {}

    public record InfluencerInfo(
            String name,
            String introduction,
            String activityName,
            List<String> platforms
    ) implements DefaultInfo {}

    public record ExistInfo(
            String title,
            String offerBudget,
            String startDate,
            String endDate,
            String overView,
            String request,
            String contentTopic // 인플루언서 전용 필드 (자영업자일 경우 null)
    ) {}
}
