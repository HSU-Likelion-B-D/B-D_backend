package com.likelion.bd.domain.businessman.web.dto;

public record WorkPlaceUpdateInitRes(
        String workPlaceName,
        String address,
        String detailAddress,
        String openTime,
        String closeTime,
        String minBudget,
        String maxBudget
) {
}
