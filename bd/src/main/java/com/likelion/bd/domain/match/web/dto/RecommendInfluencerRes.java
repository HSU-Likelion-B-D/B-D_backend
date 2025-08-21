package com.likelion.bd.domain.match.web.dto;

import java.util.List;

public record RecommendInfluencerRes(
        String imgUrl,
        String nickname,
        String avgScore,
        Long reviewCount,
        List<String> platform,
        Long followerCount,
        String minBudget,
        List<String> contentTopic,
        Integer recommendScore
) {
}
