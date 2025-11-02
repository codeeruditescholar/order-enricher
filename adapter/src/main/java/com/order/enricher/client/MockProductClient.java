package com.order.enricher.client;

import com.order.enricher.domain.Product;
import com.order.enricher.domain.exception.ProductNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class MockProductClient implements ProductClient {

  /*TODO
  This is a mock client to fetch product data from other product micro-service
  In future we can make rest call (or grpc even)
  //Also in future should put circuit breaker from resilience4j or something similar
  //can also be replaced by FeignClient
   */

  // NOTE: For transparency I have used AI here as well to generate the following mock data

  static final UUID PRODUCT_ID_WIRELESS_MOUSE =
      UUID.fromString("11111111-1111-1111-1111-111111111111");
  static final UUID PRODUCT_ID_MECHANICAL_KEYBOARD =
      UUID.fromString("22222222-2222-2222-2222-222222222222");
  static final UUID PRODUCT_ID_USB_C_CABLE =
      UUID.fromString("33333333-3333-3333-3333-333333333333");
  static final UUID PRODUCT_ID_LAPTOP_STAND =
      UUID.fromString("44444444-4444-4444-4444-444444444444");
  static final UUID PRODUCT_ID_HEADPHONES = UUID.fromString("55555555-5555-5555-5555-555555555555");
  static final UUID PRODUCT_ID_DESK_LAMP = UUID.fromString("66666666-6666-6666-6666-666666666666");
  static final UUID PRODUCT_ID_WEBCAM = UUID.fromString("77777777-7777-7777-7777-777777777777");
  static final UUID PRODUCT_ID_MONITOR = UUID.fromString("88888888-8888-8888-8888-888888888888");
  static final UUID PRODUCT_ID_ERGONOMIC_CHAIR =
      UUID.fromString("99999999-9999-9999-9999-999999999999");
  static final UUID PRODUCT_ID_PORTABLE_SSD =
      UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

  private static final Map<UUID, Product> DUMMY_PRODUCTS =
      Map.of(
          PRODUCT_ID_WIRELESS_MOUSE,
          Product.builder()
              .id(PRODUCT_ID_WIRELESS_MOUSE)
              .name("Wireless Mouse")
              .price(29.99)
              .category("Electronics")
              .tags(List.of("computer", "accessories", "wireless"))
              .build(),
          PRODUCT_ID_MECHANICAL_KEYBOARD,
          Product.builder()
              .id(PRODUCT_ID_MECHANICAL_KEYBOARD)
              .name("Mechanical Keyboard")
              .price(89.99)
              .category("Electronics")
              .tags(List.of("computer", "accessories", "gaming"))
              .build(),
          PRODUCT_ID_USB_C_CABLE,
          Product.builder()
              .id(PRODUCT_ID_USB_C_CABLE)
              .name("USB-C Cable")
              .price(12.99)
              .category("Electronics")
              .tags(List.of("cable", "usb", "accessories"))
              .build(),
          PRODUCT_ID_LAPTOP_STAND,
          Product.builder()
              .id(PRODUCT_ID_LAPTOP_STAND)
              .name("Laptop Stand")
              .price(45.00)
              .category("Office")
              .tags(List.of("ergonomic", "desk", "accessories"))
              .build(),
          PRODUCT_ID_HEADPHONES,
          Product.builder()
              .id(PRODUCT_ID_HEADPHONES)
              .name("Noise Cancelling Headphones")
              .price(199.99)
              .category("Electronics")
              .tags(List.of("audio", "wireless", "premium"))
              .build(),
          PRODUCT_ID_DESK_LAMP,
          Product.builder()
              .id(PRODUCT_ID_DESK_LAMP)
              .name("Desk Lamp")
              .price(34.50)
              .category("Office")
              .tags(List.of("lighting", "desk", "led"))
              .build(),
          PRODUCT_ID_WEBCAM,
          Product.builder()
              .id(PRODUCT_ID_WEBCAM)
              .name("Webcam HD")
              .price(79.99)
              .category("Electronics")
              .tags(List.of("video", "streaming", "conference"))
              .build(),
          PRODUCT_ID_MONITOR,
          Product.builder()
              .id(PRODUCT_ID_MONITOR)
              .name("Monitor 27 inch")
              .price(299.99)
              .category("Electronics")
              .tags(List.of("display", "4k", "computer"))
              .build(),
          PRODUCT_ID_ERGONOMIC_CHAIR,
          Product.builder()
              .id(PRODUCT_ID_ERGONOMIC_CHAIR)
              .name("Ergonomic Chair")
              .price(249.00)
              .category("Office")
              .tags(List.of("furniture", "ergonomic", "comfort"))
              .build(),
          PRODUCT_ID_PORTABLE_SSD,
          Product.builder()
              .id(PRODUCT_ID_PORTABLE_SSD)
              .name("Portable SSD 1TB")
              .price(119.99)
              .category("Electronics")
              .tags(List.of("storage", "portable", "usb"))
              .build());

  // AI-generated code ends

  @Override
  public List<Product> fetchProductsByIds(List<UUID> ids) {
    List<UUID> missingProductIds =
        ids.stream().filter(id -> !DUMMY_PRODUCTS.containsKey(id)).toList();
    if (!missingProductIds.isEmpty()) {
      throw new ProductNotFoundException(missingProductIds.stream().map(UUID::toString).toList());
    }

    return ids.stream().map(DUMMY_PRODUCTS::get).toList();
  }
}
