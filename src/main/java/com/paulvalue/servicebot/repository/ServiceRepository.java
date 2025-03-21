package com.paulvalue.servicebot.repository;

import com.paulvalue.servicebot.model.Category;
import com.paulvalue.servicebot.model.Favor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Favor, Long> {
    List<Favor> findByCategory(Category category);
}
