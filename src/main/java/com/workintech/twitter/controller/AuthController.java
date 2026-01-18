package com.workintech.twitter.controller;

import com.workintech.twitter.dto.AuthResponse;
import com.workintech.twitter.dto.LoginRequest;
import com.workintech.twitter.dto.RegisterRequest;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.service.JwtService;
import com.workintech.twitter.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        User user = new User();
        user.setFullName(registerRequest.getFullName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setUsername(registerRequest.getUsername());
        
        User registeredUser = userService.register(user);
        String token = jwtService.generateToken(registeredUser);
        
        return new ResponseEntity<>(new AuthResponse(token, registeredUser), HttpStatus.CREATED);
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        User user = userService.findByEmail(loginRequest.getEmail());
        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            String token = jwtService.generateToken(user);
            return new ResponseEntity<>(new AuthResponse(token, user), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
