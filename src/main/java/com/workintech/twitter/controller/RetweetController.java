package com.workintech.twitter.controller;

import com.workintech.twitter.dto.RetweetRequest;
import com.workintech.twitter.entity.Retweet;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.service.RetweetService;
import com.workintech.twitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/retweet")
public class RetweetController extends BaseController {

    private final RetweetService retweetService;

    @Autowired
    public RetweetController(RetweetService retweetService, UserService userService) {
        super(userService);
        this.retweetService = retweetService;
    }

    @PostMapping
    public ResponseEntity<Retweet> retweet(@RequestBody RetweetRequest request, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Retweet retweet = retweetService.retweet(user.getId(), request.getTweetId());
        return new ResponseEntity<>(retweet, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRetweet(@PathVariable Long id, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        retweetService.deleteRetweet(id, user.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
