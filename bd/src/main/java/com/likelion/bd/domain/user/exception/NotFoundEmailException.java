package com.likelion.bd.domain.user.exception;

import com.likelion.bd.global.exception.BaseException;

public class NotFoundEmailException extends BaseException {
    public NotFoundEmailException() {
        super(UserErrorCode.USER_NOT_FOUND_EMAIL_404);
    }
}
