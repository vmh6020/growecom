package com.kevin.growecom.repository;

import com.kevin.growecom.model.Cart;
import com.kevin.growecom.model.User;
import com.kevin.growecom.util.enums.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserAndStatus(User user, CartStatus status);
}
