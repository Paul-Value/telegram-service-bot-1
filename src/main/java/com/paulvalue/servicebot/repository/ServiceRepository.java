package com.paulvalue.servicebot.repository;

import com.paulvalue.servicebot.model.Category;
import com.paulvalue.servicebot.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByCategory(Category category);
}
