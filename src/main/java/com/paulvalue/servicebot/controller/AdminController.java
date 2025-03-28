package com.paulvalue.servicebot.controller;

import com.paulvalue.servicebot.model.Category;
import com.paulvalue.servicebot.model.Favor;
import com.paulvalue.servicebot.model.OrderRequest;
import com.paulvalue.servicebot.repository.FavorRepository;
import com.paulvalue.servicebot.service.CatalogService;
import com.paulvalue.servicebot.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final CatalogService catalogService;
    private final OrderService orderService;
    private final FavorRepository favorRepository;

    @PostMapping("/services")
    public ResponseEntity<Favor> createService(@RequestBody Favor service) {
        return ResponseEntity.ok(favorRepository.save(service));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderRequest>> getAllOrders() {
        return ResponseEntity.ok(orderRepository.findAll());
    }

    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        if (dto.getParentId() != null) {
            Category parent = catalogService.getCategoryById(dto.getParentId());
            category.setParent(parent);
        }
        return ResponseEntity.ok(catalogService.saveCategory(category));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderRequest>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}
