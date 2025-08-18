package com.likelion.bd.domain.user.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class ProfileCreateReq {

    @NotNull(message = "회원 ID는 필수 입력 값 입니다.")
    private Long userId;

    @NotBlank(message = "닉네임은 필수 입력 값 입니다.")
    private String nickname;

    private MultipartFile profileImage;

    private String introduction;
}
