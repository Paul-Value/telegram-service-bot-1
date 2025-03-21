package com.paulvalue.servicebot.service;

import com.paulvalue.servicebot.model.OrderRequest;
import com.paulvalue.servicebot.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderRequest createOrder(Long serviceId, String phone) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        OrderRequest order = new OrderRequest();
        order.setService(service);
        order.setPhone(phone);
        return orderRepository.save(order);
    }
}
