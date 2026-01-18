package com.workintech.twitter.service;

import com.workintech.twitter.entity.Retweet;
import com.workintech.twitter.entity.Tweet;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.repository.RetweetRepository;
import com.workintech.twitter.repository.TweetRepository;
import com.workintech.twitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RetweetServiceImpl implements RetweetService {

    private final RetweetRepository retweetRepository;
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;

    @Autowired
    public RetweetServiceImpl(RetweetRepository retweetRepository, UserRepository userRepository, TweetRepository tweetRepository) {
        this.retweetRepository = retweetRepository;
        this.userRepository = userRepository;
        this.tweetRepository = tweetRepository;
    }

    @Override
    @Transactional
    public Retweet retweet(Long userId, Long tweetId) {
        Optional<Retweet> existingRetweet = retweetRepository.findByUserIdAndTweetId(userId, tweetId);
        if (existingRetweet.isPresent()) {
            throw new RuntimeException("You have already retweeted this tweet");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new RuntimeException("Tweet not found"));

        Retweet retweet = new Retweet();
        retweet.setUser(user);
        retweet.setTweet(tweet);
        return retweetRepository.save(retweet);
    }

    @Override
    @Transactional
    public void deleteRetweet(Long id, Long userId) {
        Retweet retweet = retweetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Retweet not found"));
        
        if (!retweet.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not authorized to delete this retweet");
        }
        
        retweetRepository.delete(retweet);
    }
}
