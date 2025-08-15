package com.likelion.bd.domain.influencer.web.controller;

import com.likelion.bd.domain.influencer.service.InfluencerService;
import com.likelion.bd.domain.influencer.web.dto.ActivityCreateReq;
import com.likelion.bd.domain.influencer.web.dto.ActivityCreateRes;
import com.likelion.bd.domain.influencer.web.dto.ActivityUpdateReq;
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
@RequestMapping("/api/influencer")
public class InfluencerController {

    private final InfluencerService influencerService;

    @PostMapping("/activities") // 인플루언서 활동 생성
    public ResponseEntity<SuccessResponse<?>> createActivity(
            @RequestBody @Valid ActivityCreateReq activityCreateReq
    ) {

        ActivityCreateRes activityCreateRes = influencerService.createActivity(activityCreateReq);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.ok(activityCreateRes));
    }

    @PutMapping("/activities") // 인플루언서 활동 수정
    public ResponseEntity<SuccessResponse<?>> updateActivity(
            @RequestBody @Valid ActivityUpdateReq activityUpdateReq,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        influencerService.updateActivity(activityUpdateReq, userPrincipal.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.emptyCustom("인플루언서 정보 수정에 성공하셨습니다."));
    }

    @GetMapping("/mypage") // 인플루언서 활동 정보 수정
    public  ResponseEntity<SuccessResponse<?>> myPage(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        InfluencerMyPageRes influencerMyPageRes = influencerService.myPage(userPrincipal.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(influencerMyPageRes));
    }
}
