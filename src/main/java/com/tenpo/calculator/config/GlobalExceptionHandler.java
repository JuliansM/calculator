package com.tenpo.calculator.config;

import com.tenpo.calculator.exception.BusinessException;
import com.tenpo.calculator.exception.TechnicalException;
import com.tenpo.calculator.model.dto.StandardResponse;
import com.tenpo.calculator.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<StandardResponse> handleBadRequestExceptions(BusinessException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(StandardResponse.builder()
                        .message(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(TechnicalException.class)
    public ResponseEntity<StandardResponse> handleBadRequestExceptions(TechnicalException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(StandardResponse.builder()
                        .message(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardResponse> handleBadRequestExceptions(Exception exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(StandardResponse.builder()
                        .message(Constants.INTERNAL_ERROR_MESSAGE)
                        .build());
    }

}
