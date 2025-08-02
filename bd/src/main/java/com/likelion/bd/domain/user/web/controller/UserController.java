package com.likelion.bd.domain.user.web.controller;

import com.likelion.bd.domain.user.service.UserService;
import com.likelion.bd.domain.user.web.dto.UserSignupReq;
import com.likelion.bd.domain.user.web.dto.UserSignupRes;
import com.likelion.bd.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse<?>> signup(
            @ModelAttribute @Valid UserSignupReq userSignupReq
    ) {
        // 서비스
        UserSignupRes userSignupRes = userService.signup(userSignupReq);

        // 반환
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.from(userSignupRes));
    }
}
