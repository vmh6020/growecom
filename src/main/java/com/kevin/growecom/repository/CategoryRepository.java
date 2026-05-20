package com.kevin.growecom.repository;

import com.kevin.growecom.model.Category;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    //find , exists, count, delete + by -> auto have implementation no more code
    boolean existsByName(String name);
}
