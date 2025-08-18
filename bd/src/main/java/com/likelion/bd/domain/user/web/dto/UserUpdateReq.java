package com.likelion.bd.domain.user.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateReq { // 프로필 수정은 전부 null 가능

    // 3개 다 null 이면 -> 예전꺼 사용
    private String nickname;
    private MultipartFile profileImage;
    private String introduction;

    private boolean imageDelete; // 원래 있던 프로필 사진 없애고, 기본 프로필 사용하고 싶을 때
    private boolean introductionDelete; // 원래 있던 설명글 없애고 싶을 때
}
