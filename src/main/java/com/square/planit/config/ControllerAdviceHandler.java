package com.square.planit.config;


import com.square.planit.common.error.ErrorCode;
import com.square.planit.common.error.ErrorResponse;
import com.square.planit.common.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ControllerAdviceHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBase(BaseException ex, HttpServletRequest req) {
        ErrorCode code = ex.getErrorCode();

        String message = messageSource.getMessage(
                code.getMessageKey(),
                null,
                LocaleContextHolder.getLocale()
        );

        ErrorResponse body = ErrorResponse.of(code, message, req.getRequestURI());
        log.warn("[{}] {}", code.name(), message);
        return ResponseEntity.status(code.getHttpStatus()).body(body);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                          HttpServletRequest req) {

        List<String> messages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatRequiredMessage)
                .toList();

        String combinedMessage = String.join(", ", messages);

        ErrorResponse body = ErrorResponse.of(
                ErrorCode.VALIDATION_FAILED,
                combinedMessage,
                req.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * 필드명 기반으로 일관된 에러 메시지를 생성
     */
    private String formatRequiredMessage(FieldError error) {
        String field = error.getField();
        return messageSource.getMessage(
                ErrorCode.VALIDATION_FAILED.getMessageKey(),
                new Object[]{field},
                LocaleContextHolder.getLocale()
        );
    }
}


