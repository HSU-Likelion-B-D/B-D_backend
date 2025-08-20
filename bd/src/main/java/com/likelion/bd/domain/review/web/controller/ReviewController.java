package com.likelion.bd.domain.review.web.controller;

import com.likelion.bd.domain.review.service.ReviewService;
import com.likelion.bd.domain.review.web.dto.ReviewCreateReq;
import com.likelion.bd.global.jwt.UserPrincipal;
import com.likelion.bd.global.response.SuccessResponse;
import com.likelion.bd.global.response.code.SuccessResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/write")
    public ResponseEntity<SuccessResponse<?>> createReview(
            @RequestBody ReviewCreateReq reviewCreateReq,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        reviewService.createReview(reviewCreateReq, userPrincipal);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.of(SuccessResponseCode.SUCCESS_CREATED, "리뷰가 성공적으로 작성되었습니다."));
    }
}
