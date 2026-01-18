package com.workintech.twitter.controller;

import com.workintech.twitter.dto.CreateTweetRequest;
import com.workintech.twitter.entity.Tweet;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.service.TweetService;
import com.workintech.twitter.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tweet")
public class TweetController extends BaseController {

    private final TweetService tweetService;

    @Autowired
    public TweetController(TweetService tweetService, UserService userService) {
        super(userService);
        this.tweetService = tweetService;
    }

    @PostMapping
    public ResponseEntity<Tweet> createTweet(@Valid @RequestBody CreateTweetRequest request, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Tweet tweet = new Tweet();
        tweet.setContent(request.getContent());
        
        Tweet savedTweet = tweetService.save(tweet, user.getId());
        return new ResponseEntity<>(savedTweet, HttpStatus.CREATED);
    }

    @GetMapping("/findByUserId/{userId}")
    public List<Tweet> findByUserId(@PathVariable Long userId) {
        return tweetService.findAllByUserId(userId);
    }

    @GetMapping("/findById/{id}")
    public Tweet findById(@PathVariable Long id) {
        return tweetService.findById(id);
    }

    @PutMapping("/{id}")
    public Tweet updateTweet(@PathVariable Long id, @Valid @RequestBody CreateTweetRequest request, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Tweet tweet = new Tweet();
        tweet.setContent(request.getContent());
        return tweetService.update(id, tweet, user.getId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable Long id, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        tweetService.delete(id, user.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
