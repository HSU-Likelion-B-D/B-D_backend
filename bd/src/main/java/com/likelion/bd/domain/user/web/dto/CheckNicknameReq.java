package com.likelion.bd.domain.user.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckNicknameReq {

    @NotBlank(message = "닉네임은 필수 입력 값 입니다.")
    private String nickname;
}
