package com.likelion.bd.domain.businessman.web.controller;

import com.likelion.bd.domain.businessman.service.WorkPlaceService;
import com.likelion.bd.domain.businessman.web.dto.WorkPlaceCreateReq;
import com.likelion.bd.domain.businessman.web.dto.WorkPlaceCreateRes;
import com.likelion.bd.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class WorkPlaceController {

    private final WorkPlaceService workPlaceService;

    @PostMapping("/{userId}/workplace")
    public ResponseEntity<SuccessResponse<?>> createWorkPlace(@RequestBody WorkPlaceCreateReq workPlaceCreateReq, @PathVariable Long userId) {
        WorkPlaceCreateRes workPlaceCreateRes = workPlaceService.createWorkPlace(workPlaceCreateReq, userId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.ok(workPlaceCreateRes));
    }
}
