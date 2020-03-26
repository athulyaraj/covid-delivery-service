package com.covid.support.deliveryservice.exceptions;

import com.covid.support.deliveryservice.models.ResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ResponseModel> handleCustomException(CustomException e) {
        final ResponseModel failureResponse = ResponseModel.builder()
                .status(e.code)
                .message(e.message)
                .build();
        return ResponseEntity.status(e.getHttpStatus()).body(failureResponse);
    }
}
