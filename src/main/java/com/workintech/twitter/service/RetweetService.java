package com.workintech.twitter.service;

import com.workintech.twitter.entity.Retweet;

public interface RetweetService {
    Retweet retweet(Long userId, Long tweetId);
    void deleteRetweet(Long id, Long userId);
}
