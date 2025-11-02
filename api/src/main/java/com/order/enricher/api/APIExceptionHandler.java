package com.order.enricher.api;

import com.order.enricher.domain.ErrorResponse;
import com.order.enricher.domain.exception.CustomerNotFoundException;
import com.order.enricher.domain.exception.OrderAlreadyExistException;
import com.order.enricher.domain.exception.OrderNotFoundException;
import com.order.enricher.domain.exception.ProductNotFoundException;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
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

  @ExceptionHandler(OrderAlreadyExistException.class)
  public ResponseEntity<ErrorResponse> handle(OrderAlreadyExistException ex) {
    ErrorResponse errorResponse =
        ErrorResponse.builder().message(ex.getMessage()).timestamp(LocalDateTime.now()).build();
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(OrderNotFoundException.class)
  public ResponseEntity<ErrorResponse> handle(OrderNotFoundException ex) {
    ErrorResponse errorResponse =
        ErrorResponse.builder().message(ex.getMessage()).timestamp(LocalDateTime.now()).build();
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleEverything(Exception ex) {
    log.error(ex.getMessage(), ex);
    ErrorResponse errorResponse =
        ErrorResponse.builder().message(ex.getMessage()).timestamp(LocalDateTime.now()).build();
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
