package com.kevin.growecom.service;

import com.kevin.growecom.auth.UserPrinciple;
import com.kevin.growecom.model.User;
import com.kevin.growecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(name).orElseThrow(() -> new UsernameNotFoundException("User not found:  " + name));
        return new UserPrinciple(user);
    }
}
