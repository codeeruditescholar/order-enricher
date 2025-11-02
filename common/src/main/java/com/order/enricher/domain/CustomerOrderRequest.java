package com.order.enricher.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Getter
@Builder
@RequiredArgsConstructor
public class CustomerOrderRequest {

  private final UUID orderId;
  private final UUID customerId;
  private final List<UUID> productIds;
  private final LocalDateTime timestamp;
}
