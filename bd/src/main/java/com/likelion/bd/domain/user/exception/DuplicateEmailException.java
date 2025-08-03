package com.likelion.bd.domain.user.exception;

import com.likelion.bd.global.exception.BaseException;

public class DuplicateEmailException extends BaseException {
    public DuplicateEmailException() {
        super(UserErrorCode.USER_DUPLICATE_EMAIL_409);
    }
}
