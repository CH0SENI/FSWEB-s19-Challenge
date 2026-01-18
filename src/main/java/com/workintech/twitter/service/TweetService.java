package com.workintech.twitter.service;

import com.workintech.twitter.entity.Tweet;

import java.util.List;

public interface TweetService {
    Tweet save(Tweet tweet, Long userId);
    List<Tweet> findAllByUserId(Long userId);
    Tweet findById(Long id);
    Tweet update(Long id, Tweet tweet, Long userId);
    void delete(Long id, Long userId);
}
