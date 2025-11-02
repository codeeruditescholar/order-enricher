package com.order.enricher.domain;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "customer_order")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerOrder {

  @Id private UUID id;

  // Ok this needs explanation and discussion
  // I am treating customer and products as snapshot at time of order
  // I do not see any point in creating separate tables and creating relationship
  // ofcourse this is up for discussion based on functional and technical requirements
  // will be happy to discuss during a call

  @Type(JsonType.class)
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private Customer customer;

  @Type(JsonType.class)
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private List<Product> products;

  private LocalDateTime timestamp;
}
