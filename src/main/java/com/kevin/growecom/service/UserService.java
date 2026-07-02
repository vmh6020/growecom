package com.kevin.growecom.service;

import com.kevin.growecom.dto.auth.RegisterRequest;
import com.kevin.growecom.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User create(User user);
    Page<User> findAll(Pageable pageable);
    void update(User user);
    User findById(Long id);
    User findByEmail(String email);
    void deleteById(Long id);
    User registerNewUser(RegisterRequest request);
    User getCurrentUser();
}