package com.order.enricher.client;

import static com.order.enricher.client.MockCustomerClient.CUSTOMER_ID_JOHN_DOE;
import static org.assertj.core.api.Assertions.*;

import com.order.enricher.domain.Customer;
import com.order.enricher.domain.exception.CustomerNotFoundException;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class MockCustomerClientTest {

  private final MockCustomerClient customerClient = new MockCustomerClient();

  @Test
  void fetchCustomerById_shouldReturnCustomer_whenCustomerExists() {
    // when
    Customer customer = customerClient.fetchCustomerById(CUSTOMER_ID_JOHN_DOE);

    // then
    assertThat(customer).isNotNull();
  }

  @Test
  void fetchCustomerById_shouldThrowCustomerNotFoundException_whenCustomerDoesNotExist() {
    // given
    UUID nonExistingCustomerId = UUID.fromString("00000000-0000-0000-0000-000000000000");

    // when & then
    assertThatThrownBy(() -> customerClient.fetchCustomerById(nonExistingCustomerId))
        .isInstanceOf(CustomerNotFoundException.class)
        .hasMessage("Customer with id " + nonExistingCustomerId + " not found");
  }
}
