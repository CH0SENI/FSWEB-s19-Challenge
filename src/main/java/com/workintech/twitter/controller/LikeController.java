package com.workintech.twitter.controller;

import com.workintech.twitter.dto.LikeRequest;
import com.workintech.twitter.entity.Like;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.service.LikeService;
import com.workintech.twitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LikeController extends BaseController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService, UserService userService) {
        super(userService);
        this.likeService = likeService;
    }

    @PostMapping("/like")
    public ResponseEntity<Like> likeTweet(@RequestBody LikeRequest request, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Like like = likeService.likeTweet(user.getId(), request.getTweetId());
        return new ResponseEntity<>(like, HttpStatus.CREATED);
    }

    @PostMapping("/dislike")
    public ResponseEntity<Void> dislikeTweet(@RequestBody LikeRequest request, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        likeService.dislikeTweet(user.getId(), request.getTweetId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
