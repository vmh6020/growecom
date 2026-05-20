package com.kevin.growecom.service.blueprint;

import com.kevin.growecom.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User create(User user);
    Page<User> findAll(Pageable pageable);
    void update(User user);
    User findById(Long id);
    void deleteById(Long id);
}