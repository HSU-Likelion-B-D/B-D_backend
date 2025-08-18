package com.likelion.bd.domain.user.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSigninReq {

    @Email(message = "유효한 이메일 주소가 아닙니다.")
    @NotBlank(message = "이메일은 필수 입력 값 입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값 입니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*[^A-Za-z0-9]).{4,12}$",
            message = "비밀번호는 영문자와 특수문자를 포함하여 4~12자여야 합니다.")
    private String password;
}
