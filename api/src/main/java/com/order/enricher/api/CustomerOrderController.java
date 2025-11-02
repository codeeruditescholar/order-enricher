package com.order.enricher.api;

import com.order.enricher.domain.CustomerOrder;
import com.order.enricher.domain.CustomerOrderRequest;
import com.order.enricher.service.CustomerOrderService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class CustomerOrderController {

  private final CustomerOrderService orderService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CustomerOrder enrich(@RequestBody CustomerOrderRequest orderRequest) {
    return orderService.createOrder(orderRequest);
  }

  @GetMapping("/{orderId}")
  @ResponseStatus(HttpStatus.OK)
  public CustomerOrder get(@PathVariable("orderId") UUID orderId) {
    return orderService.fetchOrderById(orderId);
  }
}
