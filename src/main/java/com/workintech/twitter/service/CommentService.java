package com.workintech.twitter.service;

import com.workintech.twitter.entity.Comment;

public interface CommentService {
    Comment save(Comment comment, Long userId, Long tweetId);
    Comment update(Long id, Comment comment, Long userId);
    void delete(Long id, Long userId);
}
