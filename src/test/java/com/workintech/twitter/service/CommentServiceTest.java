package com.workintech.twitter.service;

import com.workintech.twitter.entity.Comment;
import com.workintech.twitter.entity.Tweet;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.repository.CommentRepository;
import com.workintech.twitter.repository.TweetRepository;
import com.workintech.twitter.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TweetRepository tweetRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private User user;
    private Tweet tweet;
    private Comment comment;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        tweet = new Tweet();
        tweet.setId(1L);
        tweet.setUser(user); // Tweet sahibi user

        comment = new Comment();
        comment.setId(1L);
        comment.setContent("Nice tweet!");
        comment.setUser(user); // Yorum sahibi user
        comment.setTweet(tweet);
    }

    @Test
    void save_ShouldCreateComment() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(tweet));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // Act
        Comment savedComment = commentService.save(comment, 1L, 1L);

        // Assert
        assertNotNull(savedComment);
        assertEquals("Nice tweet!", savedComment.getContent());
    }

    @Test
    void delete_ShouldDeleteComment_WhenUserIsCommentOwner() {
        // Arrange
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        // Act
        commentService.delete(1L, 1L);

        // Assert
        verify(commentRepository).delete(comment);
    }
}
