package com.order.enricher.service;

import static com.order.enricher.client.MockCustomerClient.CUSTOMER_ID_JOHN_DOE;
import static com.order.enricher.client.MockProductClient.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.order.enricher.client.MockCustomerClient;
import com.order.enricher.client.MockProductClient;
import com.order.enricher.domain.CustomerOrder;
import com.order.enricher.domain.CustomerOrderRequest;
import com.order.enricher.domain.exception.CustomerNotFoundException;
import com.order.enricher.domain.exception.OrderAlreadyExistException;
import com.order.enricher.domain.exception.OrderNotFoundException;
import com.order.enricher.domain.exception.ProductNotFoundException;
import com.order.enricher.repository.CustomerOrderRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// NOTE: For transparency I have used AI here to generate and then spent some time to clean up

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomerOrderService Tests")
class CustomerOrderServiceTest {

  // Use real mock implementations instead of Mockito mocks
  private final MockCustomerClient customerClient = new MockCustomerClient();
  private final MockProductClient productClient = new MockProductClient();

  // Only mock the repository since we don't have a real implementation
  @Mock private CustomerOrderRepository customerOrderRepository;

  private CustomerOrderService customerOrderService;

  private UUID orderId;
  private UUID customerId;
  private List<UUID> productIds;
  private LocalDateTime timestamp;
  private CustomerOrderRequest orderRequest;

  @BeforeEach
  void setUp() {
    // Initialize service with real mock clients
    customerOrderService =
        new CustomerOrderService(customerClient, productClient, customerOrderRepository);

    orderId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    customerId = CUSTOMER_ID_JOHN_DOE; // Use constant from MockCustomerClient
    productIds = List.of(PRODUCT_ID_WIRELESS_MOUSE, PRODUCT_ID_MECHANICAL_KEYBOARD);
    timestamp = LocalDateTime.of(2025, 11, 1, 14, 30, 0);

    orderRequest =
        CustomerOrderRequest.builder()
            .orderId(orderId)
            .customerId(customerId)
            .productIds(productIds)
            .timestamp(timestamp)
            .build();
  }

  @Nested
  @DisplayName("createOrder Tests")
  class CreateOrderTests {

    @Test
    @DisplayName("Should successfully create order when all data is valid")
    void createOrder_shouldSucceed_whenAllDataIsValid() {
      // given
      when(customerOrderRepository.findById(orderId)).thenReturn(Optional.empty());
      when(customerOrderRepository.save(any(CustomerOrder.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      // when
      CustomerOrder result = customerOrderService.createOrder(orderRequest);

      // then
      assertThat(result).isNotNull();

      // verify repository interactions
      verify(customerOrderRepository).findById(orderId);
      verify(customerOrderRepository).save(any(CustomerOrder.class));
    }

    @Test
    @DisplayName("Should throw OrderAlreadyExistException when order already exists")
    void createOrder_shouldThrowOrderAlreadyExistException_whenOrderExists() {
      // given
      CustomerOrder existingOrder =
          CustomerOrder.builder()
              .id(orderId)
              .customer(customerClient.fetchCustomerById(customerId))
              .products(productClient.fetchProductsByIds(productIds))
              .timestamp(timestamp)
              .build();

      when(customerOrderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

      // when & then
      assertThatThrownBy(() -> customerOrderService.createOrder(orderRequest))
          .isInstanceOf(OrderAlreadyExistException.class);

      // verify that save was never called
      verify(customerOrderRepository).findById(orderId);
      verify(customerOrderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when product IDs are invalid")
    void createOrder_shouldThrowProductNotFoundException_whenProductIdsInvalid() {
      // given - use invalid product IDs that don't exist in MockProductClient
      UUID invalidProductId = UUID.fromString("00000000-0000-0000-0000-000000000000");
      List<UUID> invalidProductIds = List.of(invalidProductId);

      CustomerOrderRequest invalidRequest =
          CustomerOrderRequest.builder()
              .orderId(orderId)
              .customerId(customerId)
              .productIds(invalidProductIds)
              .timestamp(timestamp)
              .build();

      when(customerOrderRepository.findById(orderId)).thenReturn(Optional.empty());

      // when & then
      assertThatThrownBy(() -> customerOrderService.createOrder(invalidRequest))
          .isInstanceOf(ProductNotFoundException.class)
          .hasMessageContaining("00000000-0000-0000-0000-000000000000");

      // verify
      verify(customerOrderRepository).findById(orderId);
      verify(customerOrderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw CustomerNotFoundException when customer ID is invalid")
    void createOrder_shouldThrowCustomerNotFoundException_whenCustomerIdInvalid() {
      // given - use invalid customer ID that doesn't exist in MockCustomerClient
      UUID invalidCustomerId = UUID.fromString("00000000-0000-0000-0000-000000000000");

      CustomerOrderRequest invalidRequest =
          CustomerOrderRequest.builder()
              .orderId(orderId)
              .customerId(invalidCustomerId)
              .productIds(productIds)
              .timestamp(timestamp)
              .build();

      when(customerOrderRepository.findById(orderId)).thenReturn(Optional.empty());

      // when & then
      assertThatThrownBy(() -> customerOrderService.createOrder(invalidRequest))
          .isInstanceOf(CustomerNotFoundException.class)
          .hasMessageContaining(invalidCustomerId.toString());

      // verify
      verify(customerOrderRepository).findById(orderId);
      verify(customerOrderRepository, never()).save(any());
    }
  }

  @Nested
  @DisplayName("fetchOrderById Tests")
  class FetchOrderByIdTests {

    @Test
    @DisplayName("Should successfully fetch order when order exists")
    void fetchOrderById_shouldReturnOrder_whenOrderExists() {
      // given
      CustomerOrder existingOrder =
          CustomerOrder.builder()
              .id(orderId)
              .customer(customerClient.fetchCustomerById(customerId))
              .products(productClient.fetchProductsByIds(productIds))
              .timestamp(timestamp)
              .build();

      when(customerOrderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

      // when
      CustomerOrder result = customerOrderService.fetchOrderById(orderId);

      // then
      assertThat(result).isNotNull();
      verify(customerOrderRepository).findById(orderId);
    }

    @Test
    @DisplayName("Should throw OrderNotFoundException when order does not exist")
    void fetchOrderById_shouldThrowOrderNotFoundException_whenOrderDoesNotExist() {
      // given
      UUID nonExistentOrderId = UUID.fromString("99999999-9999-9999-9999-999999999999");
      when(customerOrderRepository.findById(nonExistentOrderId)).thenReturn(Optional.empty());

      // when & then
      assertThatThrownBy(() -> customerOrderService.fetchOrderById(nonExistentOrderId))
          .isInstanceOf(OrderNotFoundException.class)
          .hasMessageContaining(nonExistentOrderId.toString());

      verify(customerOrderRepository).findById(nonExistentOrderId);
    }
  }
}
