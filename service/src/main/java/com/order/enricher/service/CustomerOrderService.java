package com.order.enricher.service;

import com.order.enricher.client.CustomerClient;
import com.order.enricher.client.ProductClient;
import com.order.enricher.domain.Customer;
import com.order.enricher.domain.CustomerOrder;
import com.order.enricher.domain.CustomerOrderRequest;
import com.order.enricher.domain.Product;
import com.order.enricher.repository.CustomerOrderRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CustomerOrderService {

  private final CustomerClient customerClient;

  private final ProductClient productClient;

  private final CustomerOrderRepository customerOrderRepository;

  @Transactional(rollbackFor = Exception.class)
  public CustomerOrder createOrder(final CustomerOrderRequest orderRequest) {
    // TODO validate existing order before creating new
    // TODO use redis cache to load products and customers when not mocked data
    List<Product> products = productClient.fetchProductsByIds(orderRequest.getProductIds());
    Customer customer = customerClient.fetchCustomerById(orderRequest.getCustomerId());

    CustomerOrder enrichedOrder =
        CustomerOrder.builder()
            .id(orderRequest.getOrderId())
            .customer(customer)
            .products(products)
            .timestamp(orderRequest.getTimestamp())
            .build();

    customerOrderRepository.save(enrichedOrder);

    return enrichedOrder;
  }
}
