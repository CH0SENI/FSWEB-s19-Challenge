package com.workintech.twitter.service;

import com.workintech.twitter.entity.Tweet;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.repository.TweetRepository;
import com.workintech.twitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    @Autowired
    public TweetServiceImpl(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Tweet save(Tweet tweet, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        tweet.setUser(user);
        return tweetRepository.save(tweet);
    }

    @Override
    public List<Tweet> findAllByUserId(Long userId) {
        // TODO: Tweet sayısı arttığında performans sorunu olabilir. Pagination (Pageable) eklenmeli.
        return tweetRepository.findByUserId(userId);
    }

    @Override
    public Tweet findById(Long id) {
        return tweetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tweet not found"));
    }

    @Override
    @Transactional
    public Tweet update(Long id, Tweet tweet, Long userId) {
        Tweet existingTweet = findById(id);
        
        // Sadece tweet sahibi güncelleyebilir kontrolü
        if (!existingTweet.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not authorized to update this tweet");
        }
        
        if (tweet.getContent() != null) {
            existingTweet.setContent(tweet.getContent());
        }
        return tweetRepository.save(existingTweet);
    }

    @Override
    @Transactional
    public void delete(Long id, Long userId) {
        Tweet existingTweet = findById(id);
        
        // Sadece tweet sahibi silebilir
        if (!existingTweet.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not authorized to delete this tweet");
        }
        tweetRepository.delete(existingTweet);
    }
}
