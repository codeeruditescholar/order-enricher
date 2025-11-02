package com.order.enricher.client;

import com.order.enricher.domain.Address;
import com.order.enricher.domain.Customer;
import com.order.enricher.domain.exception.CustomerNotFoundException;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class MockCustomerClient implements CustomerClient {

  /*TODO
  This is a mock client to fetch customer data from other customer micro-service
  In future we can make rest call (or grpc even)
  //Also in future should put circuit breaker from resilience4j or something similar
  //can also be replaced by FeignClient
   */

  // NOTE: For transparency I have used AI to generate the following mock data

  public static final UUID CUSTOMER_ID_JOHN_DOE =
      UUID.fromString("a1b2c3d4-e5f6-4a5b-8c9d-1e2f3a4b5c6d");
  static final UUID CUSTOMER_ID_JANE_SMITH =
      UUID.fromString("b2c3d4e5-f6a7-5b6c-9d0e-2f3a4b5c6d7e");
  static final UUID CUSTOMER_ID_ROBERT_JOHNSON =
      UUID.fromString("c3d4e5f6-a7b8-6c7d-0e1f-3a4b5c6d7e8f");
  static final UUID CUSTOMER_ID_EMILY_WILLIAMS =
      UUID.fromString("d4e5f6a7-b8c9-7d8e-1f2a-4b5c6d7e8f9a");
  static final UUID CUSTOMER_ID_MICHAEL_BROWN =
      UUID.fromString("e5f6a7b8-c9d0-8e9f-2a3b-5c6d7e8f9a0b");

  private static final Map<UUID, Customer> DUMMY_CUSTOMERS =
      Map.of(
          CUSTOMER_ID_JOHN_DOE,
          Customer.builder()
              .id(CUSTOMER_ID_JOHN_DOE)
              .firstName("John")
              .lastName("Doe")
              .address(
                  Address.builder()
                      .street("123 Main St")
                      .city("New York")
                      .state("NY")
                      .zip("10001")
                      .country("USA")
                      .build())
              .build(),
          CUSTOMER_ID_JANE_SMITH,
          Customer.builder()
              .id(CUSTOMER_ID_JANE_SMITH)
              .firstName("Jane")
              .lastName("Smith")
              .address(
                  Address.builder()
                      .street("456 Oak Ave")
                      .city("Los Angeles")
                      .state("CA")
                      .zip("90001")
                      .country("USA")
                      .build())
              .build(),
          CUSTOMER_ID_ROBERT_JOHNSON,
          Customer.builder()
              .id(CUSTOMER_ID_ROBERT_JOHNSON)
              .firstName("Robert")
              .lastName("Johnson")
              .address(
                  Address.builder()
                      .street("789 Pine Rd")
                      .city("Chicago")
                      .state("IL")
                      .zip("60601")
                      .country("USA")
                      .build())
              .build(),
          CUSTOMER_ID_EMILY_WILLIAMS,
          Customer.builder()
              .id(CUSTOMER_ID_EMILY_WILLIAMS)
              .firstName("Emily")
              .lastName("Williams")
              .address(
                  Address.builder()
                      .street("321 Elm St")
                      .city("Houston")
                      .state("TX")
                      .zip("77001")
                      .country("USA")
                      .build())
              .build(),
          CUSTOMER_ID_MICHAEL_BROWN,
          Customer.builder()
              .id(CUSTOMER_ID_MICHAEL_BROWN)
              .firstName("Michael")
              .lastName("Brown")
              .address(
                  Address.builder()
                      .street("654 Maple Dr")
                      .city("Phoenix")
                      .state("AZ")
                      .zip("85001")
                      .country("USA")
                      .build())
              .build());

  // AI-generated code ends

  @Override
  public Customer fetchCustomerById(UUID id) {
    Customer customer = DUMMY_CUSTOMERS.get(id);
    if (customer == null) {
      throw new CustomerNotFoundException(id);
    }
    return customer;
  }
}
