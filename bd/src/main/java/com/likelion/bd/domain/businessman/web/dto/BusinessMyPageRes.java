package com.likelion.bd.domain.businessman.web.dto;

import java.util.List;

public record BusinessMyPageRes(
        String nickname,
        String workPlaceName,
        String address,
        String detailAddress,
        String introduce,
        String openTime,
        String closeTime,
        String avgScore,
        List<String> categoryList,
        List<String> moodList,
        List<String> promotionList
) {
}
