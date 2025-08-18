package com.likelion.bd.domain.businessman.service;

import com.likelion.bd.domain.businessman.web.dto.*;
import com.likelion.bd.global.jwt.UserPrincipal;

public interface BusinessManService {
    WorkPlaceCreateRes createWorkPlace(WorkPlaceCreateReq workPlaceCreateReq);
    WorkPlaceUpdateRes updateWorkPlace(WorkPlaceUpdateReq workPlaceUpdateReq, UserPrincipal userPrincipal);
    BusinessMyPageRes mypage(UserPrincipal userPrincipal);

    BusinessHomeRes home(UserPrincipal userPrincipal);
}
