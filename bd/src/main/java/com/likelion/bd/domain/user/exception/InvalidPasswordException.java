package com.likelion.bd.domain.user.exception;

import com.likelion.bd.global.exception.BaseException;

public class InvalidPasswordException extends BaseException {
    public InvalidPasswordException() {
        super(UserErrorCode.USER_INVALID_PASSWORD_401);
    }
}
