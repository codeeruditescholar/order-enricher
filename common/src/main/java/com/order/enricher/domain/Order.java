package com.order.enricher.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@Builder
@RequiredArgsConstructor
public class Order {

  private final UUID id;
  private final Customer customer;
  private final List<Product> products;
  private final LocalDateTime timestamp;
}
