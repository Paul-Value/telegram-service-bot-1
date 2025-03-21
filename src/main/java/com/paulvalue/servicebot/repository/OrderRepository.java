package com.paulvalue.servicebot.repository;

import com.paulvalue.servicebot.model.OrderRequest;
import com.paulvalue.servicebot.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderRequest, Long> {
    List<OrderRequest> findByStatus(OrderStatus status);
}
