package com.paulvalue.servicebot.service;

import com.paulvalue.servicebot.model.Favor;
import com.paulvalue.servicebot.model.OrderRequest;
import com.paulvalue.servicebot.repository.OrderRepository;
import com.paulvalue.servicebot.repository.FavorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final FavorRepository favorRepository;

    public OrderRequest createOrder(Long serviceId, String phone) {
        Favor service = favorRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        OrderRequest order = new OrderRequest();
        order.setFavor(service);
        order.setPhone(phone);
        return orderRepository.save(order);
    }
}
