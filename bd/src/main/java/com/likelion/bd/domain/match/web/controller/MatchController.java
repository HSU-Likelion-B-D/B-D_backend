package com.likelion.bd.domain.match.web.controller;

import com.likelion.bd.domain.businessman.entity.BusinessMan;
import com.likelion.bd.domain.businessman.service.BusinessManService;
import com.likelion.bd.domain.match.service.MatchService;
import com.likelion.bd.domain.match.web.dto.RecommendInfluencerRes;
import com.likelion.bd.global.jwt.UserPrincipal;
import com.likelion.bd.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MatchController {

    private final MatchService matchService;

    @GetMapping("/businessman/me/recommendations")
    public ResponseEntity<SuccessResponse<?>> influencerRecommend(@AuthenticationPrincipal UserPrincipal userPrincipal){

        List<RecommendInfluencerRes> recommendList = matchService.top5ForBusinessMan(userPrincipal.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(recommendList));
    }
}
