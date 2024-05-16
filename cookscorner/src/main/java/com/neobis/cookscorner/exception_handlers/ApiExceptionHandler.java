package com.neobis.cookscorner.exception_handlers;

import com.neobis.cookscorner.dtos.other.ApiErrorResponse;
import com.neobis.cookscorner.exceptions.ApiCommonException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = ApiCommonException.class)
    public ResponseEntity<ApiErrorResponse> handleApiCommonException(ApiCommonException ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(ex.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


}
