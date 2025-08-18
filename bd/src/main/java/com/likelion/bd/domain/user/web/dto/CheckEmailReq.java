package com.likelion.bd.domain.user.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckEmailReq {

    @Email(message = "유효한 이메일 주소가 아닙니다.")
    @NotBlank(message = "이메일은 필수 입력 값 입니다.")
    private String email;

    private String code;

    private String purpose;
}
