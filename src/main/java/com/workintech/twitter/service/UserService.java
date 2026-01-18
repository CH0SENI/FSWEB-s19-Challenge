package com.workintech.twitter.service;

import com.workintech.twitter.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User register(User user);
    User findByEmail(String email);
}
