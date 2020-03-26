package com.covid.support.deliveryservice.exceptions;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomException extends RuntimeException{
    String code;
    String message;
    HttpStatus httpStatus;
}
