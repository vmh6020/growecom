package com.kevin.growecom.repository;

import com.kevin.growecom.entity.Order;
import org.springframework.aot.hint.annotation.RegisterReflection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
