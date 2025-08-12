package com.likelion.bd.domain.user.web.controller;

import com.likelion.bd.domain.user.service.UserService;
import com.likelion.bd.domain.user.web.dto.*;
import com.likelion.bd.global.jwt.UserPrincipal;
import com.likelion.bd.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 이메일 중복 체크 확인
    @PostMapping("/check-email")
    public ResponseEntity<SuccessResponse<?>> check(
            @RequestBody @Valid CheckEmailReq checkEmailReq
    ) {
        userService.checkEmail(checkEmailReq);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.emptyCustom("사용 가능한 이메일입니다."));
    }

    // 닉네임 중복 체크 확인
    @PostMapping("/check-nickname")
    public ResponseEntity<SuccessResponse<?>> check(
            @RequestBody @Valid CheckNicknameReq checkNicknameReq
    ) {
        userService.checkNickname(checkNicknameReq);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.emptyCustom("사용 가능한 닉네임입니다."));
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse<?>> signup(
            @ModelAttribute @Valid UserSignupReq userSignupReq
    ) {
        // 서비스
        UserSignupRes userSignupRes = userService.signup(userSignupReq);

        // 반환
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.ok(userSignupRes));
    }

    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<SuccessResponse<?>> signin(
            @RequestBody @Valid UserSigninReq userSigninReq
    ) {
        // 서비스
        UserSigninRes userSigninRes = userService.signin(userSigninReq);

        // 반환
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(userSigninRes));
    }

    // 회원 정보 수정
    @PutMapping("/update")
    public ResponseEntity<SuccessResponse<?>> update(
            @ModelAttribute @Valid UserUpdateReq userUpdateReq,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        userService.updateUser(userUpdateReq, userPrincipal.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.emptyCustom("회원 정보 수정에 성공하셨습니다."));
    }
}
