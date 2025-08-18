package com.likelion.bd.domain.user.service;

import com.likelion.bd.domain.user.web.dto.*;

public interface UserService {

    // 이메일 중복 체크 확인
    void checkEmail(CheckEmailReq checkEmailReq);

    // 닉네임 중복 체크 확인
    void checkNickname(CheckNicknameReq checkNicknameReq);

    // 회원 가입
    UserSignupRes signup(UserSignupReq userSignupReq);

    // 프로필 생성
    void profileCreate(ProfileCreateReq profileCreateReq);

    // 로그인
    UserSigninRes signin(UserSigninReq userSigninReq);

    // 회원 정보 수정
    void updateUser(UserUpdateReq userUpdateReq, Long userId);

    // 이메일로 인증번호 전송
    void sendCodeToEmail(CheckEmailReq checkEmailReq);

    // 인증번호 검증
    void verifyCode(CheckEmailReq checkEmailReq);

    // 비밀번호 변경
    void changePassword(UserSigninReq userSigninReq);
}
