package com.hcmue.handle.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.hcmue.dto.HttpResponseError;

@RestControllerAdvice
public class TokenControllerAdvice {

    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public HttpResponseError handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        return new HttpResponseError(
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                request.getDescription(false));
    }
}
