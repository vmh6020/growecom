package com.kevin.growecom.service;

import com.kevin.growecom.entity.Order;
import com.kevin.growecom.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private EntityManager entityManager;
    @Autowired
    public UserServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    public void save(Order order) {
        entityManager.persist(order);
    }

    @Override
    public void update(Order order) {
        entityManager.merge(order);
    }

    @Override
    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void deleteById(Long id) {
        User user = findById(id);
       entityManager.remove(user);
    }
}
