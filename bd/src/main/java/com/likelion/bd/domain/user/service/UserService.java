package com.likelion.bd.domain.user.service;

import com.likelion.bd.domain.user.web.dto.CheckEmailReq;
import com.likelion.bd.domain.user.web.dto.UserSignupReq;
import com.likelion.bd.domain.user.web.dto.UserSignupRes;

public interface UserService {

    // 이메일 중복 체크 확인
    void checkEmail(CheckEmailReq checkEmailReq);

    // 회원 가입
    UserSignupRes signup(UserSignupReq userSignupReq);
}
