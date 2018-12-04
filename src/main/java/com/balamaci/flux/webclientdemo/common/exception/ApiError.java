package com.balamaci.flux.webclientdemo.common.exception;

import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

/**
 * @author sbalamaci
 */
@ToString
public class ApiError {

    private String errorId;

    private HttpStatus status;
    private String message;
    private List<String> errors;

    public ApiError() {
    }

    public ApiError(HttpStatus status, String message, List<String> errors) {
        errorId = UUID.randomUUID().toString();

        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public String getErrorId() {
        return errorId;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getErrors() {
        return errors;
    }

}
