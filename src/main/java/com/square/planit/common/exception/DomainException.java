package com.square.planit.common.exception;


import com.square.planit.common.error.ErrorCode;

public class DomainException extends BaseException {
    public DomainException(ErrorCode code) {
        super(code);
    }
}