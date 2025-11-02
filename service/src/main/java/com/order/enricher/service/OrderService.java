package com.order.enricher.service;

import com.order.enricher.client.CustomerClient;
import com.order.enricher.client.ProductClient;
import com.order.enricher.domain.Customer;
import com.order.enricher.domain.Order;
import com.order.enricher.domain.OrderRequest;
import com.order.enricher.domain.Product;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderService {

  private final CustomerClient customerClient;

  private final ProductClient productClient;

  // private final OrderRepository orderRepository;

  public Order createOrder(final OrderRequest orderRequest) {
    // TODO use redis cache
    List<Product> products = productClient.fetchProductsByIds(orderRequest.getProductIds());
    Customer customer = customerClient.fetchCustomerById(orderRequest.getCustomerId());
    Order enrichedOrder =
        Order.builder()
            .id(orderRequest.getOrderId())
            .customer(customer)
            .products(products)
            .timestamp(orderRequest.getTimestamp())
            .build();

    return enrichedOrder;
  }
}
