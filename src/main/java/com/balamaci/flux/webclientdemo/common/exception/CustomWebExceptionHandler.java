package com.balamaci.flux.webclientdemo.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

/**
 * @author sbalamaci
 */
@RestControllerAdvice
@Slf4j
public class CustomWebExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApiError> handleDuplicateEntityException(DuplicateEntityException ex) {
        log.info("Custom global exception handler: Handling DuplicateEntityException from global");

        ApiError apiError = new ApiError(HttpStatus.CONFLICT, "Duplicate entity found",
                Collections.emptyList());

        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }


}
