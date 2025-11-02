package com.order.enricher.client;

import static com.order.enricher.client.MockProductClient.*;
import static org.assertj.core.api.Assertions.*;

import com.order.enricher.domain.Product;
import com.order.enricher.domain.exception.ProductNotFoundException;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class MockProductClientTest {

  private final MockProductClient productClient = new MockProductClient();

  @Test
  void fetchProductsByIds_shouldReturnAllProducts_whenAllIdsExist() {
    // given
    List<UUID> validIds =
        List.of(PRODUCT_ID_WIRELESS_MOUSE, PRODUCT_ID_MECHANICAL_KEYBOARD, PRODUCT_ID_USB_C_CABLE);

    // when
    List<Product> products = productClient.fetchProductsByIds(validIds);

    // then
    assertThat(products)
        .hasSize(3)
        .extracting(Product::getName)
        .containsExactly("Wireless Mouse", "Mechanical Keyboard", "USB-C Cable");
  }

  @Test
  void fetchProductsByIds_shouldReturnEmptyList_whenIdsListIsEmpty() {
    // given
    List<UUID> emptyIds = List.of();

    // when
    List<Product> products = productClient.fetchProductsByIds(emptyIds);

    // then
    assertThat(products).isEmpty();
  }

  @Test
  void fetchProductsByIds_shouldThrowProductNotFoundException_whenOneIdDoesNotExist() {
    // given
    UUID nonExistentId = UUID.fromString("00000000-0000-0000-0000-000000000000");
    List<UUID> idsWithInvalid =
        List.of(
            PRODUCT_ID_WIRELESS_MOUSE,
            nonExistentId, // non-existent
            PRODUCT_ID_MECHANICAL_KEYBOARD);

    // when & then
    assertThatThrownBy(() -> productClient.fetchProductsByIds(idsWithInvalid))
        .isInstanceOf(ProductNotFoundException.class)
        .hasMessageContaining("00000000-0000-0000-0000-000000000000");
  }
}
