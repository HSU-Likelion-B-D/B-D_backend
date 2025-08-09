package com.likelion.bd.domain.businessman.web.dto;

import java.util.List;

public record WorkPlaceUpdateRes(
        Long businessManId,
        String name,
        String address,
        String detailAddress,
        String openTime,
        String closeTime,
        Boolean isOnline,
        List<Long> categoryIds,
        List<Long> moodIds,
        List<Long> promotionIds
) {
}
