package com.likelion.bd.domain.campaign.web.controller;

import com.likelion.bd.domain.campaign.entity.CampaignStatus;
import com.likelion.bd.domain.campaign.service.CampaignService;
import com.likelion.bd.domain.campaign.web.dto.CampaignCreateReq;
import com.likelion.bd.domain.campaign.web.dto.CampaignListRes;
import com.likelion.bd.global.jwt.UserPrincipal;
import com.likelion.bd.global.response.SuccessResponse;
import com.likelion.bd.global.response.code.SuccessResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/campaigns")
public class CampaignController {
    private final CampaignService campaignService;

    @PostMapping
    public ResponseEntity<SuccessResponse<?>> createCampaign(
            @RequestBody CampaignCreateReq campaignCreateReq,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        campaignService.createCampaign(campaignCreateReq, userPrincipal);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.of(SuccessResponseCode.SUCCESS_CREATED, "제안서 전송에 성공하셨습니다."));
    }

    @GetMapping
    /*
     * GET /api/campaigns?state=WAITING&page=3
     * - 모두보기: /api/campaigns?all=true
     * - 특정상태: /api/campaigns?state=WAITING   (all=false 또는 미포함)
     * - 프론트는 드롭다운에서 코드 문자열만 넘김 (라벨은 프론트에서 표시)
     * 기본 페이징: size=3
     */
    public ResponseEntity<SuccessResponse<?>> showCampaigns
            (@AuthenticationPrincipal UserPrincipal userPrincipal,
             @RequestParam(required = false) CampaignStatus status, //state를 선택적 파라미터로 변경
             @RequestParam(defaultValue = "false") boolean all,
             @PageableDefault(size = 3) Pageable pageable) {
        Page<CampaignListRes> campaignListRes = campaignService.showCampaign(userPrincipal, status, all, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(campaignListRes));
    }
}
