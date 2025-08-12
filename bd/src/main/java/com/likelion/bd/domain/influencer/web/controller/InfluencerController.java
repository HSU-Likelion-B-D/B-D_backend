package com.likelion.bd.domain.influencer.web.controller;

import com.likelion.bd.domain.influencer.service.InfluencerService;
import com.likelion.bd.domain.influencer.web.dto.ActivityCreateReq;
import com.likelion.bd.domain.influencer.web.dto.ActivityCreateRes;
import com.likelion.bd.domain.influencer.web.dto.InfluencerMyPageRes;
import com.likelion.bd.global.jwt.UserPrincipal;
import com.likelion.bd.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/influencer")
public class InfluencerController {

    private final InfluencerService influencerService;

    @PostMapping("/create")
    public ResponseEntity<SuccessResponse<?>> createActivity(
            @RequestBody @Valid ActivityCreateReq activityCreateReq,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        ActivityCreateRes activityCreateRes = influencerService.createActivity(activityCreateReq, userPrincipal.getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.ok(activityCreateRes));
    }

    @GetMapping("/mypage")
    public  ResponseEntity<SuccessResponse<?>> myPage(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        InfluencerMyPageRes influencerMyPageRes = influencerService.myPage(userPrincipal.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(influencerMyPageRes));
    }
}
