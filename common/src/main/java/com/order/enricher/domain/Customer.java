package com.order.enricher.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@RequiredArgsConstructor
public class Customer {

  private final UUID id; // customer unique id
  private final String name;
  @JsonIgnore private final String firstName;
  @JsonIgnore private final String lastName;
  private final Address address;

  // TODO perhaps validate on creation for required attributes
  public String getName() {
    return firstName + " " + lastName;
  }
}
