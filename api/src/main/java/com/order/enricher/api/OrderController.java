package com.order.enricher.api;

import com.order.enricher.domain.Order;
import com.order.enricher.domain.OrderRequest;
import com.order.enricher.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Order enrich(@RequestBody OrderRequest orderRequest) {
    return orderService.createOrder(orderRequest);
  }
}
