package com.kevin.growecom.service;

import com.kevin.growecom.security.UserPrinciple;

public interface JwtService {
    String generateToken(UserPrinciple principle);
}
