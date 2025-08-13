package com.likelion.bd.domain.businessman.web.controller;

import com.likelion.bd.domain.businessman.service.BusinessManService;
import com.likelion.bd.domain.businessman.web.dto.*;
import com.likelion.bd.global.jwt.UserPrincipal;
import com.likelion.bd.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/businessman")
public class BusinessManController {

    private final BusinessManService businessManService;

    @PostMapping("/workplaces")
    public ResponseEntity<SuccessResponse<?>> createWorkPlace
            (@RequestBody WorkPlaceCreateReq workPlaceCreateReq) {
        WorkPlaceCreateRes workPlaceCreateRes = businessManService.createWorkPlace(workPlaceCreateReq);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.ok(workPlaceCreateRes));
    }

    @PutMapping("/workplaces")
    public ResponseEntity<SuccessResponse<?>> updateWorkPlace
            (@RequestBody WorkPlaceUpdateReq workPlaceUpdateReq,
             @AuthenticationPrincipal UserPrincipal userPrincipal) {
        WorkPlaceUpdateRes workPlaceUpdateRes = businessManService.updateWorkPlace(workPlaceUpdateReq, userPrincipal);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(workPlaceUpdateRes));
    }

    @GetMapping("/mypage")
    public ResponseEntity<SuccessResponse<?>> myPage(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        BusinessMyPageRes businessMyPageRes = businessManService.mypage(userPrincipal);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(businessMyPageRes));
    }

    @GetMapping("/home")
    public ResponseEntity<SuccessResponse<?>> home(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        BusinessHomeRes businessHome = businessManService.home(userPrincipal);
    }
}
