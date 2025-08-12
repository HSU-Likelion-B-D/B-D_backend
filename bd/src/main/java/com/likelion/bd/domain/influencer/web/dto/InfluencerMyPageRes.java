package com.likelion.bd.domain.influencer.web.dto;

import java.util.List;

public record InfluencerMyPageRes(
        String imgUrl,                  // 사진
        String activityName,            // 활동명
        String nickName,                // 닉네임
        String follower,                // 팔로워 수
        Double avgScore,                // 평점
        String snsUrl,                  // SNS 링크
        Long minAmount,                 // 희망하는 최소 금액
        List<String> platforms,        // 플랫폼 목록
        List<String> contentTopics,    // 컨텐츠 분야 목록
        List<String> contentStyles,    // 컨텐츠 스타일 목록
        List<String> preferTopics      // 선호 분야 목록
) {

}