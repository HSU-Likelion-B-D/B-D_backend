package com.likelion.bd.domain.businessman.service;

import com.likelion.bd.domain.businessman.web.dto.WorkPlaceCreateReq;
import com.likelion.bd.domain.businessman.web.dto.WorkPlaceCreateRes;
import com.likelion.bd.domain.businessman.web.dto.WorkPlaceUpdateReq;
import com.likelion.bd.domain.businessman.web.dto.WorkPlaceUpdateRes;
import com.likelion.bd.global.jwt.UserPrincipal;

public interface WorkPlaceService {
    WorkPlaceCreateRes createWorkPlace(WorkPlaceCreateReq workPlaceCreateReq);
    WorkPlaceUpdateRes updateWorkPlace(WorkPlaceUpdateReq workPlaceUpdateReq, UserPrincipal userPrincipal);
}
