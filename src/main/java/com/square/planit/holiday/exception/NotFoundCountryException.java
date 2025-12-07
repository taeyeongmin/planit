package com.square.planit.holiday.exception;

import com.square.planit.common.error.ErrorCode;
import com.square.planit.common.exception.DomainException;

public class NotFoundCountryException extends DomainException {

    public NotFoundCountryException() {
        super(ErrorCode.NOT_FOUND_COUNTRY);
    }
}