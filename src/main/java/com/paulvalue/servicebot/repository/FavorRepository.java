package com.paulvalue.servicebot.repository;

import com.paulvalue.servicebot.model.Category;
import com.paulvalue.servicebot.model.Favor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavorRepository extends JpaRepository<Favor, Long> {
    List<Favor> findByCategory(Category category);
}
