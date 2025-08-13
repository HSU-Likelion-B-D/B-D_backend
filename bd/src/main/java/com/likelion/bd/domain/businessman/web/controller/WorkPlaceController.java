package com.likelion.bd.domain.businessman.web.controller;

import com.likelion.bd.domain.businessman.service.WorkPlaceService;
import com.likelion.bd.domain.businessman.web.dto.WorkPlaceCreateReq;
import com.likelion.bd.domain.businessman.web.dto.WorkPlaceCreateRes;
import com.likelion.bd.domain.businessman.web.dto.WorkPlaceUpdateReq;
import com.likelion.bd.domain.businessman.web.dto.WorkPlaceUpdateRes;
import com.likelion.bd.global.jwt.UserPrincipal;
import com.likelion.bd.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/businessman/workplaces")
public class WorkPlaceController {

    private final WorkPlaceService workPlaceService;

    @PostMapping
    public ResponseEntity<SuccessResponse<?>> createWorkPlace
            (@RequestBody WorkPlaceCreateReq workPlaceCreateReq) {
        WorkPlaceCreateRes workPlaceCreateRes = workPlaceService.createWorkPlace(workPlaceCreateReq);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.ok(workPlaceCreateRes));
    }

    @PutMapping
    public ResponseEntity<SuccessResponse<?>> updateWorkPlace
            (@RequestBody WorkPlaceUpdateReq workPlaceUpdateReq,
             @AuthenticationPrincipal UserPrincipal userPrincipal) {
        WorkPlaceUpdateRes workPlaceUpdateRes = workPlaceService.updateWorkPlace(workPlaceUpdateReq, userPrincipal);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(workPlaceUpdateRes));
    }
}
