package com.likelion.bd.domain.campaign.web.controller;

import com.likelion.bd.domain.campaign.service.ProposalService;
import com.likelion.bd.domain.campaign.web.dto.ProposalFormRes;
import com.likelion.bd.domain.campaign.web.dto.ProposalWriteReq;
import com.likelion.bd.domain.campaign.web.dto.ProposalWriteRes;
import com.likelion.bd.global.jwt.UserPrincipal;
import com.likelion.bd.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/proposal")
@RequiredArgsConstructor
public class ProposalController {

    private final ProposalService proposalService;

    @GetMapping("write")
    public ResponseEntity<SuccessResponse<?>> getForm(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        ProposalFormRes proposalFormRes = proposalService.getProposalForm(userPrincipal);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.ok(proposalFormRes));
    }

    @PostMapping("write")
    public ResponseEntity<SuccessResponse<?>> write(
            @RequestBody @Valid ProposalWriteReq proposalWriteReq,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        ProposalWriteRes proposalWriteRes = proposalService.writeProposal(proposalWriteReq, userPrincipal);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.okCustom(proposalWriteRes, "제안서 작성에 성공하셨습니다."));
    }
}
