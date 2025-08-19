package com.likelion.bd.domain.match.web.dto;

import java.util.List;

public record RecommendInfluencerRes(
        String imgUrl,
        String nickname,
        String avgScore,
        Long reviewCount,
        List<String> platform,
        Long followerCount,
        Long minBudget,
        List<String> contentTopic,
        Integer recommendScore
) {
}
