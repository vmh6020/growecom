package com.kevin.growecom.service;

import com.kevin.growecom.entity.Order;
import com.kevin.growecom.entity.User;

public interface UserService {
    void save(Order order);
    void update(Order order);
    User findById(Long id);
    void deleteById(Long id);
}
