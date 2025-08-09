package com.likelion.bd.domain.businessman.web.controller;

import com.likelion.bd.domain.businessman.service.WorkPlaceService;
import com.likelion.bd.domain.businessman.web.dto.WorkPlaceCreateReq;
import com.likelion.bd.domain.businessman.web.dto.WorkPlaceCreateRes;
import com.likelion.bd.domain.businessman.web.dto.WorkPlaceUpdateReq;
import com.likelion.bd.domain.businessman.web.dto.WorkPlaceUpdateRes;
import com.likelion.bd.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workplace")
public class WorkPlaceController {

    private final WorkPlaceService workPlaceService;

    @PostMapping("/{userId}")
    public ResponseEntity<SuccessResponse<?>> createWorkPlace(@RequestBody WorkPlaceCreateReq workPlaceCreateReq, @PathVariable Long userId) {
        WorkPlaceCreateRes workPlaceCreateRes = workPlaceService.createWorkPlace(workPlaceCreateReq, userId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.ok(workPlaceCreateRes));
    }

    @PutMapping("/{workPlaceId}")
    public ResponseEntity<SuccessResponse<?>> updateWorkPlace(@PathVariable Long workPlaceId, @RequestBody WorkPlaceUpdateReq workPlaceUpdateReq) {
        //우선 JWT는 제외하고 개발함 JWT는 나중에 추가하면 될듯
        WorkPlaceUpdateRes workPlaceUpdateRes = workPlaceService.updateWorkPlace(workPlaceUpdateReq, workPlaceId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(SuccessResponse.ok(workPlaceUpdateRes));
    }
}
