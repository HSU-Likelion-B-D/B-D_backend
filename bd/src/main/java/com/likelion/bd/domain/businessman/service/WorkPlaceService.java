package com.likelion.bd.domain.businessman.service;

import com.likelion.bd.domain.businessman.web.dto.WorkPlaceCreateReq;
import com.likelion.bd.domain.businessman.web.dto.WorkPlaceCreateRes;

public interface WorkPlaceService {
    WorkPlaceCreateRes createWorkPlace(WorkPlaceCreateReq workPlaceCreateReq, Long userId);
}
