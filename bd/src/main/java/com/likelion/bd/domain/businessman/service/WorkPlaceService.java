package com.likelion.bd.domain.businessman.service;

import com.likelion.bd.domain.businessman.web.dto.WorkPlaceCreateReq;
import com.likelion.bd.domain.businessman.web.dto.WorkPlaceCreateRes;
import com.likelion.bd.domain.businessman.web.dto.WorkPlaceUpdateReq;

public interface WorkPlaceService {
    WorkPlaceCreateRes createWorkPlace(WorkPlaceCreateReq workPlaceCreateReq, Long userId);
    void updateWorkPlace(WorkPlaceUpdateReq workPlaceUpdateReq, Long workPlaceId);
}
