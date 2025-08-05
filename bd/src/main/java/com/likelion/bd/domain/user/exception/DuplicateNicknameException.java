package com.likelion.bd.domain.user.exception;

import com.likelion.bd.global.exception.BaseException;

public class DuplicateNicknameException extends BaseException {
    public DuplicateNicknameException() {
        super(UserErrorCode.USER_DUPLICATE_NICKNAME_409);
    }
}
