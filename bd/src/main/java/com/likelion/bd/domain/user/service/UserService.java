package com.likelion.bd.domain.user.service;

import com.likelion.bd.domain.user.web.dto.UserSignupReq;
import com.likelion.bd.domain.user.web.dto.UserSignupRes;

public interface UserService {

    // 회원 가입
    UserSignupRes signup(UserSignupReq userSignupReq);
}
