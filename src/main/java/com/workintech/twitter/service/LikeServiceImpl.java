package com.workintech.twitter.service;

import com.workintech.twitter.entity.Like;
import com.workintech.twitter.entity.Tweet;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.repository.LikeRepository;
import com.workintech.twitter.repository.TweetRepository;
import com.workintech.twitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;

    @Autowired
    public LikeServiceImpl(LikeRepository likeRepository, UserRepository userRepository, TweetRepository tweetRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.tweetRepository = tweetRepository;
    }

    @Override
    @Transactional
    public Like likeTweet(Long userId, Long tweetId) {
        Optional<Like> existingLike = likeRepository.findByUserIdAndTweetId(userId, tweetId);
        if (existingLike.isPresent()) {
            throw new RuntimeException("You have already liked this tweet");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new RuntimeException("Tweet not found"));

        Like like = new Like();
        like.setUser(user);
        like.setTweet(tweet);
        return likeRepository.save(like);
    }

    @Override
    @Transactional
    public void dislikeTweet(Long userId, Long tweetId) {
        Like like = likeRepository.findByUserIdAndTweetId(userId, tweetId)
                .orElseThrow(() -> new RuntimeException("Like not found"));
        
        likeRepository.delete(like);
    }
}
