package com.likelion.bd.domain.match.web.dto;

import java.util.List;

public record RecommandInfluencerRes(
        String imgUrl,
        String nickname,
        String avgScore,
        String reviewCount,
        List<String> platform,
        Long followerCount,
        Long minBudget,
        List<String> contentTopic
) {
}
