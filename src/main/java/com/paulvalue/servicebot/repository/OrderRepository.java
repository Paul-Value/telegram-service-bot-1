package com.paulvalue.servicebot.repository;

import com.paulvalue.servicebot.model.OrderRequest;
import com.paulvalue.servicebot.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderRequest, Long> {
    List<OrderRequest> findByStatus(OrderStatus status);
}
