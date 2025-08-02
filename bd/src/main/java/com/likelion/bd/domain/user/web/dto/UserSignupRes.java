package com.likelion.bd.domain.user.web.dto;

import com.likelion.bd.domain.user.entity.UserRoleType;

public record UserSignupRes(
        Long userId,
        UserRoleType userRoleType) {
}
