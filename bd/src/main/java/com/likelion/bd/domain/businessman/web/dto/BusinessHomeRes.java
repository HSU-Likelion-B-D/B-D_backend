package com.likelion.bd.domain.businessman.web.dto;

import java.util.List;

public record BusinessHomeRes(
        String imgUrl,
        String nickname,
        String workPlaceName,
        String avgScore,
        Long reviewCount,
        List<InfluencerSummaryRes> Influencers
) {
    public record InfluencerSummaryRes(
            Long InfluencerId,
            String nickName,
            String imgUrl,
            String follower
    ) {

    }
}
