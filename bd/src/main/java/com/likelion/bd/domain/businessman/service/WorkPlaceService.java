package com.likelion.bd.domain.businessman.service;

import com.likelion.bd.domain.businessman.web.dto.WorkPlaceCreateReq;
import com.likelion.bd.domain.businessman.web.dto.WorkPlaceCreateRes;
import com.likelion.bd.domain.businessman.web.dto.WorkPlaceUpdateReq;
import com.likelion.bd.domain.businessman.web.dto.WorkPlaceUpdateRes;

public interface WorkPlaceService {
    WorkPlaceCreateRes createWorkPlace(WorkPlaceCreateReq workPlaceCreateReq, Long userId);
    WorkPlaceUpdateRes updateWorkPlace(WorkPlaceUpdateReq workPlaceUpdateReq, Long workPlaceId);
}
