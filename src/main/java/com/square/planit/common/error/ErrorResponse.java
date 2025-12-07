package com.square.planit.common.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Builder
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final String timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;

    public static ErrorResponse of(ErrorCode code, String message, String path) {
        return ErrorResponse.builder()
                .timestamp(OffsetDateTime.now().toString())
                .status(code.getHttpStatus().value())
                .error(code.getHttpStatus().getReasonPhrase())
                .message(message)
                .path(path)
                .build();
    }
}