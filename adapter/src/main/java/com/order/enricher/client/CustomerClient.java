package com.order.enricher.client;

import com.order.enricher.domain.Customer;
import java.util.UUID;

public interface CustomerClient {
  Customer fetchCustomerById(UUID id);
}
