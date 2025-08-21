package com.likelion.bd.domain.user.web.dto;

import com.likelion.bd.domain.user.entity.UserRoleType;

public record UserSigninRes(
        String nickname,
        String imgUrl,
        UserRoleType userRoleType,
        String token
) {

}
