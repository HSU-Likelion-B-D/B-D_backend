package com.likelion.bd.domain.match.web.dto;

import java.util.List;

public record RecommendInfluencerRes(
        Long userId,
        String imgUrl,
        String nickname,
        String avgScore,
        Long reviewCount,
        List<String> platform,
        String followerCount,
        String minBudget,
        List<String> contentTopic,
        Integer recommendScore,

        String activityName,
        String introduction
) {
}
