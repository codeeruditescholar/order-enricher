package com.order.enricher.domain;

import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class Product {

  private final UUID id; // product unique id
  private final String name;
  private final double price;
  private final String category; // TODO perhaps make it enum
  private final List<String> tags;
}
