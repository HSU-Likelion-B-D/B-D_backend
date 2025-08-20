package com.likelion.bd.domain.user.web.dto;

import com.likelion.bd.domain.user.entity.UserRoleType;

public record UserSigninRes(
        UserRoleType userRoleType,
        String token
) {

}
