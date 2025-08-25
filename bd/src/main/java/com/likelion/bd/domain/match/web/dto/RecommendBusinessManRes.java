package com.likelion.bd.domain.match.web.dto;

import java.util.List;

public record RecommendBusinessManRes(
        Long userId,
        String imgUrl, //프로필이미지 url
        String nickname, //닉네임
        String avgScore, //평균 별점
        Long reviewCount, //리뷰 수
        List<String> categoryList, //가게 업종
        List<String> moodList, //가게 분위기
        String minBudget, //최소 지급 금액
        String region, //지역
        int finalScore,

        String workPlaceName,
        String introduction
) {
}
