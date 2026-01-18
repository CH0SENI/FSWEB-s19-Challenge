package com.workintech.twitter.service;

import com.workintech.twitter.entity.Like;

public interface LikeService {
    Like likeTweet(Long userId, Long tweetId);
    void dislikeTweet(Long userId, Long tweetId);
}
