package com.order.enricher.domain.exception;

import java.util.UUID;

public class OrderAlreadyExistException extends RuntimeException {

  public OrderAlreadyExistException(UUID orderId) {
    super("Order already exists: " + orderId);
  }
}
