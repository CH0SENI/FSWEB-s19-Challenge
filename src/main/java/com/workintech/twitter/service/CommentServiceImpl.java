package com.workintech.twitter.service;

import com.workintech.twitter.entity.Comment;
import com.workintech.twitter.entity.Tweet;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.repository.CommentRepository;
import com.workintech.twitter.repository.TweetRepository;
import com.workintech.twitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, TweetRepository tweetRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.tweetRepository = tweetRepository;
    }

    @Override
    @Transactional
    public Comment save(Comment comment, Long userId, Long tweetId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new RuntimeException("Tweet not found"));
        
        comment.setUser(user);
        comment.setTweet(tweet);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment update(Long id, Comment comment, Long userId) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        
        if (!existingComment.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not authorized to update this comment");
        }
        
        if (comment.getContent() != null) {
            existingComment.setContent(comment.getContent());
        }
        return commentRepository.save(existingComment);
    }

    @Override
    @Transactional
    public void delete(Long id, Long userId) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        
        boolean isCommentOwner = existingComment.getUser().getId().equals(userId);
        boolean isTweetOwner = existingComment.getTweet().getUser().getId().equals(userId);
        
        if (!isCommentOwner && !isTweetOwner) {
            throw new RuntimeException("You are not authorized to delete this comment");
        }
        
        commentRepository.delete(existingComment);
    }
}
