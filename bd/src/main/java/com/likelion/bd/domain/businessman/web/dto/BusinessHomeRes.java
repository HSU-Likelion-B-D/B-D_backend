package com.likelion.bd.domain.businessman.web.dto;

public record BusinessHomeRes(
        String imgUrl,
        String nickname,
        String workPlaceName,
        String avgScore,
        Long reviewCount
) {
}
