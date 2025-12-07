package com.square.planit.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 4xx
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "error.validation.required"),
    NOT_FOUND_COUNTRY(HttpStatus.NOT_FOUND, "error.notFoundCountry"),

    // 5xx
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "error.internal");

    private final HttpStatus httpStatus;
    private final String messageKey;
}