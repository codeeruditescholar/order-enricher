package com.order.enricher.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class Address {
  private final String street;
  private final String city;
  private final String state;
  private final String zip;
  private final String country;
}
