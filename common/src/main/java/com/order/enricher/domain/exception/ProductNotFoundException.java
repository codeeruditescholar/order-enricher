package com.order.enricher.domain.exception;

import java.util.List;

public class ProductNotFoundException extends RuntimeException {
  public ProductNotFoundException(List<String> missingIds) {
    super("Products with id(s) not found: " + String.join(",", missingIds));
  }
}
