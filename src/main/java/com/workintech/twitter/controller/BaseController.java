package com.workintech.twitter.controller;

import com.workintech.twitter.entity.User;
import com.workintech.twitter.service.UserService;
import org.springframework.security.core.Authentication;

public abstract class BaseController {

    protected final UserService userService;

    protected BaseController(UserService userService) {
        this.userService = userService;
    }

    protected User getAuthenticatedUser(Authentication authentication) {
        String email = authentication.getName();
        return userService.findByEmail(email);
    }
}
