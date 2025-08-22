package com.likelion.bd.domain.campaign.web.controller;

import com.likelion.bd.domain.campaign.entity.CampaignStatus;
import com.likelion.bd.domain.campaign.service.PaymentService;
import com.likelion.bd.domain.campaign.web.dto.PaymentListRes;
import com.likelion.bd.domain.campaign.web.dto.PaymentResponseReq;
import com.likelion.bd.global.jwt.UserPrincipal;
import com.likelion.bd.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    /*
     * GET /api/payments?state=WAITING&page=3
     * - 모두보기: /api/payments?all=true
     * - 특정상태: /api/payments?state=WAITING   (all=false 또는 미포함)
     * - 프론트는 드롭다운에서 코드 문자열만 넘김 (라벨은 프론트에서 표시)
     * 기본 페이징: size=10
     */
    public ResponseEntity<SuccessResponse<?>> showPayment(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(required = false) CampaignStatus status, // status 선택적 파라미터로 변경
            @RequestParam(defaultValue = "false") boolean all,
            @PageableDefault(size = 6) Pageable pageable
    ) {
        Page<PaymentListRes> paymentListRes = paymentService.showPayment(userPrincipal, status, all, pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(paymentListRes));
    }

    @PatchMapping
    public ResponseEntity<SuccessResponse<?>> updatePayment(
            @RequestBody @Valid PaymentResponseReq paymentRequestReq ,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        paymentService.updatePayment(userPrincipal, paymentRequestReq);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.emptyCustom("결제 상태가 성공적으로 변경되었습니다."));
    }
}
