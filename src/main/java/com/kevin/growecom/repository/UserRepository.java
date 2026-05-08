package com.kevin.growecom.repository;

import com.kevin.growecom.entity.Order;
import com.kevin.growecom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
