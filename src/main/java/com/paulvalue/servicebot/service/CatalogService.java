package com.paulvalue.servicebot.service;

import com.paulvalue.servicebot.model.Category;
import com.paulvalue.servicebot.repository.CategoryRepository;
import com.paulvalue.servicebot.repository.ServiceRepository;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CatalogService {
    private final CategoryRepository categoryRepository;
    private final ServiceRepository serviceRepository;

    public List<Category> getMainCategories() {
        return categoryRepository.findByParentIsNull();
    }

    public List<Category> getSubCategories(Long parentId) {
        Category parent = categoryRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return categoryRepository.findByParent(parent);
    }

    public List<Service> getServicesByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return serviceRepository.findByCategory(category);
    }
}
