package com.order.enricher.api;

import com.order.enricher.domain.ErrorResponse;
import com.order.enricher.domain.exception.CustomerNotFoundException;
import com.order.enricher.domain.exception.ProductNotFoundException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class APIExceptionHandler {

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<ErrorResponse> handle(ProductNotFoundException ex) {
    ErrorResponse errorResponse =
        ErrorResponse.builder().message(ex.getMessage()).timestamp(LocalDateTime.now()).build();
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(CustomerNotFoundException.class)
  public ResponseEntity<ErrorResponse> handle(CustomerNotFoundException ex) {
    ErrorResponse errorResponse =
        ErrorResponse.builder().message(ex.getMessage()).timestamp(LocalDateTime.now()).build();
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleEverything(Exception ex) {
    ErrorResponse errorResponse =
        ErrorResponse.builder().message(ex.getMessage()).timestamp(LocalDateTime.now()).build();
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
