package com.likelion.bd.domain.businessman.web.dto;

import java.util.List;

public record WorkPlaceCreateRes(
        Long businessManId,
        Long id,
        String name,
        String address,
        String openTime,
        String closeTime,
        Boolean isOnline,
        List<Long> categoryIds,
        List<Long> moodIds,
        List<Long> promotionIds
) {
}
