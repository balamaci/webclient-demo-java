package com.balamaci.flux.webclientdemo.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author sbalamaci
 */
@RestControllerAdvice
@Slf4j
public class CustomWebExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleDuplicateEntityException(DuplicateEntityException ex) {
        log.info("Custom global exception handler: Handling DuplicateEntityException from global");
        return ResponseEntity.badRequest().build();
    }


}
