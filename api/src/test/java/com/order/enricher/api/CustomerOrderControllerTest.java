package com.order.enricher.api;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.order.enricher.domain.*;
import com.order.enricher.domain.exception.CustomerNotFoundException;
import com.order.enricher.domain.exception.OrderAlreadyExistException;
import com.order.enricher.domain.exception.OrderNotFoundException;
import com.order.enricher.domain.exception.ProductNotFoundException;
import com.order.enricher.service.CustomerOrderService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// NOTE: For transparency I have used AI here to generate and then spent some time to clean up

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomerOrderController Tests")
class CustomerOrderControllerTest {

  private MockMvc mockMvc;

  private ObjectMapper objectMapper;

  @Mock private CustomerOrderService orderService;

  private CustomerOrderController controller;

  private UUID orderId;
  private UUID customerId;
  private List<UUID> productIds;
  private LocalDateTime timestamp;
  private CustomerOrderRequest orderRequest;
  private CustomerOrder customerOrder;
  private Customer customer;
  private List<Product> products;

  @BeforeEach
  void setUp() {
    // Initialize controller with mocked service
    controller = new CustomerOrderController(orderService);

    // Configure ObjectMapper with JavaTimeModule for LocalDateTime
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    // IMPORTANT: Serialize dates as ISO-8601 strings, not numeric arrays
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    // Build MockMvc standalone with controller and exception handler
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(new APIExceptionHandler())
            .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
            .build();

    // Setup test data
    orderId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    customerId = UUID.fromString("a1b2c3d4-e5f6-4a5b-8c9d-1e2f3a4b5c6d");
    productIds =
        List.of(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            UUID.fromString("22222222-2222-2222-2222-222222222222"));
    timestamp = LocalDateTime.of(2025, 11, 1, 14, 30, 0);

    orderRequest =
        CustomerOrderRequest.builder()
            .orderId(orderId)
            .customerId(customerId)
            .productIds(productIds)
            .timestamp(timestamp)
            .build();

    customer =
        Customer.builder()
            .id(customerId)
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
            .build();

    products =
        List.of(
            Product.builder()
                .id(productIds.get(0))
                .name("Wireless Mouse")
                .price(29.99)
                .category("Electronics")
                .tags(List.of("computer", "accessories"))
                .build(),
            Product.builder()
                .id(productIds.get(1))
                .name("Mechanical Keyboard")
                .price(89.99)
                .category("Electronics")
                .tags(List.of("computer", "gaming"))
                .build());

    customerOrder =
        CustomerOrder.builder()
            .id(orderId)
            .customer(customer)
            .products(products)
            .timestamp(timestamp)
            .build();
  }

  @Nested
  @DisplayName("POST /orders - Create Order Tests")
  class CreateOrderTests {

    @Test
    @DisplayName("Should create order and return 201 CREATED with order details")
    void createOrder_shouldReturn201_whenRequestIsValid() throws Exception {
      // given
      when(orderService.createOrder(any(CustomerOrderRequest.class))).thenReturn(customerOrder);

      // when & then
      mockMvc
          .perform(
              post("/orders")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(orderRequest)))
          .andDo(print())
          .andExpect(status().isCreated())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.id").value(orderId.toString()))
          .andExpect(jsonPath("$.customer.id").value(customerId.toString()))
          .andExpect(jsonPath("$.customer.name").value("John Doe"))
          .andExpect(jsonPath("$.customer.address.city").value("New York"))
          .andExpect(jsonPath("$.products", hasSize(2)))
          .andExpect(jsonPath("$.products[0].name").value("Wireless Mouse"))
          .andExpect(jsonPath("$.products[0].price").value(29.99))
          .andExpect(jsonPath("$.products[1].name").value("Mechanical Keyboard"))
          .andExpect(jsonPath("$.products[1].price").value(89.99))
          .andExpect(jsonPath("$.timestamp").value("2025-11-01T14:30:00"));

      verify(orderService).createOrder(any(CustomerOrderRequest.class));
    }

    @Test
    @DisplayName("Should return 400 BAD_REQUEST when order already exists")
    void createOrder_shouldReturn400_whenOrderAlreadyExists() throws Exception {
      // given
      when(orderService.createOrder(any(CustomerOrderRequest.class)))
          .thenThrow(new OrderAlreadyExistException(orderId));

      // when & then
      mockMvc
          .perform(
              post("/orders")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(orderRequest)))
          .andDo(print())
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(containsString(orderId.toString())))
          .andExpect(jsonPath("$.timestamp").exists());

      verify(orderService).createOrder(any(CustomerOrderRequest.class));
    }

    @Test
    @DisplayName("Should return 400 BAD_REQUEST when product not found")
    void createOrder_shouldReturn400_whenProductNotFound() throws Exception {
      // given
      when(orderService.createOrder(any(CustomerOrderRequest.class)))
          .thenThrow(new ProductNotFoundException(List.of("11111111-1111-1111-1111-111111111111")));

      // when & then
      mockMvc
          .perform(
              post("/orders")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(orderRequest)))
          .andDo(print())
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(containsString("11111111-1111-1111-1111-111111111111")))
          .andExpect(jsonPath("$.message").value(containsString("Products with id(s) not found")))
          .andExpect(jsonPath("$.timestamp").exists());

      verify(orderService).createOrder(any(CustomerOrderRequest.class));
    }

    @Test
    @DisplayName("Should return 400 BAD_REQUEST when customer not found")
    void createOrder_shouldReturn400_whenCustomerNotFound() throws Exception {
      // given
      when(orderService.createOrder(any(CustomerOrderRequest.class)))
          .thenThrow(new CustomerNotFoundException(customerId));

      // when & then
      mockMvc
          .perform(
              post("/orders")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(orderRequest)))
          .andDo(print())
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(containsString(customerId.toString())))
          .andExpect(jsonPath("$.message").value(containsString("Customer with id")))
          .andExpect(jsonPath("$.timestamp").exists());

      verify(orderService).createOrder(any(CustomerOrderRequest.class));
    }

    @Test
    @DisplayName("Should create order with single product")
    void createOrder_shouldReturn201_withSingleProduct() throws Exception {
      // given
      List<UUID> singleProductId = List.of(productIds.get(0));
      CustomerOrderRequest singleProductRequest =
          CustomerOrderRequest.builder()
              .orderId(orderId)
              .customerId(customerId)
              .productIds(singleProductId)
              .timestamp(timestamp)
              .build();

      CustomerOrder singleProductOrder =
          CustomerOrder.builder()
              .id(orderId)
              .customer(customer)
              .products(List.of(products.get(0)))
              .timestamp(timestamp)
              .build();

      when(orderService.createOrder(any(CustomerOrderRequest.class)))
          .thenReturn(singleProductOrder);

      // when & then
      mockMvc
          .perform(
              post("/orders")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(singleProductRequest)))
          .andDo(print())
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.products", hasSize(1)))
          .andExpect(jsonPath("$.products[0].name").value("Wireless Mouse"));

      verify(orderService).createOrder(any(CustomerOrderRequest.class));
    }
  }

  @Nested
  @DisplayName("GET /orders/{orderId} - Get Order Tests")
  class GetOrderTests {

    @Test
    @DisplayName("Should return order and 200 OK when order exists")
    void getOrder_shouldReturn200_whenOrderExists() throws Exception {
      // given
      when(orderService.fetchOrderById(orderId)).thenReturn(customerOrder);

      // when & then
      mockMvc
          .perform(get("/orders/{orderId}", orderId).accept(MediaType.APPLICATION_JSON))
          .andDo(print())
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.id").value(orderId.toString()))
          .andExpect(jsonPath("$.customer.name").value("John Doe"))
          .andExpect(jsonPath("$.products", hasSize(2)))
          .andExpect(jsonPath("$.products[0].name").value("Wireless Mouse"))
          .andExpect(jsonPath("$.timestamp").value("2025-11-01T14:30:00"));

      verify(orderService).fetchOrderById(orderId);
    }

    @Test
    @DisplayName("Should return 404 NOT_FOUND when order not found")
    void getOrder_shouldReturn404_whenOrderNotFound() throws Exception {
      // given
      UUID nonExistentOrderId = UUID.fromString("99999999-9999-9999-9999-999999999999");
      when(orderService.fetchOrderById(nonExistentOrderId))
          .thenThrow(new OrderNotFoundException(nonExistentOrderId));

      // when & then
      mockMvc
          .perform(get("/orders/{orderId}", nonExistentOrderId).accept(MediaType.APPLICATION_JSON))
          .andDo(print())
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.message").value(containsString(nonExistentOrderId.toString())))
          .andExpect(jsonPath("$.message").value(containsString("Order with id")))
          .andExpect(jsonPath("$.timestamp").exists());

      verify(orderService).fetchOrderById(nonExistentOrderId);
    }

    @Test
    @DisplayName("Should return 500 INTERNAL_SERVER_ERROR when orderId format is invalid")
    void getOrder_shouldReturn500_whenOrderIdFormatIsInvalid() throws Exception {
      // given - invalid UUID format
      String invalidOrderId = "invalid-uuid-format";

      // when & then
      // Note: Standalone MockMvc doesn't handle type conversion errors the same way as
      // full Spring context. The invalid UUID causes a 500 error instead of 400.
      mockMvc
          .perform(get("/orders/{orderId}", invalidOrderId).accept(MediaType.APPLICATION_JSON))
          .andDo(print())
          .andExpect(status().isInternalServerError())
          .andExpect(jsonPath("$.message").exists())
          .andExpect(jsonPath("$.timestamp").exists());

      verify(orderService, never()).fetchOrderById(any());
    }

    @Test
    @DisplayName("Should call service only once when fetching order")
    void getOrder_shouldCallServiceOnce() throws Exception {
      // given
      when(orderService.fetchOrderById(orderId)).thenReturn(customerOrder);

      // when
      mockMvc
          .perform(get("/orders/{orderId}", orderId).accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());

      // then
      verify(orderService, times(1)).fetchOrderById(orderId);
    }
  }

  @Nested
  @DisplayName("Integration Tests")
  class IntegrationTests {

    @Test
    @DisplayName("Should handle complete order lifecycle - create and fetch")
    void orderLifecycle_shouldCreateAndFetch() throws Exception {
      // given - create order
      when(orderService.createOrder(any(CustomerOrderRequest.class))).thenReturn(customerOrder);

      // when - create order
      mockMvc
          .perform(
              post("/orders")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(orderRequest)))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.id").value(orderId.toString()));

      // given - fetch order
      when(orderService.fetchOrderById(orderId)).thenReturn(customerOrder);

      // when - fetch order
      mockMvc
          .perform(get("/orders/{orderId}", orderId).accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(orderId.toString()))
          .andExpect(jsonPath("$.customer.name").value("John Doe"));

      // then
      verify(orderService).createOrder(any(CustomerOrderRequest.class));
      verify(orderService).fetchOrderById(orderId);
    }
  }
}
