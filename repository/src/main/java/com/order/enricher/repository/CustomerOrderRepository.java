package com.order.enricher.repository;

import com.order.enricher.domain.CustomerOrder;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, UUID> {}
