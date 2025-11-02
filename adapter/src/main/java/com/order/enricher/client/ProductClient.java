package com.order.enricher.client;

import com.order.enricher.domain.Product;
import java.util.List;
import java.util.UUID;

public interface ProductClient {

  List<Product> fetchProductsByIds(List<UUID> ids);
}
