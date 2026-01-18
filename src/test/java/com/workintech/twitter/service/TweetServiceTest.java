package com.workintech.twitter.service;

import com.workintech.twitter.entity.Tweet;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.repository.TweetRepository;
import com.workintech.twitter.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TweetServiceTest {

    @Mock
    private TweetRepository tweetRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TweetServiceImpl tweetService;

    private User user;
    private Tweet tweet;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        tweet = new Tweet();
        tweet.setId(1L);
        tweet.setContent("Hello World");
        tweet.setUser(user);
    }

    @Test
    void save_ShouldCreateTweet_WhenUserExists() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(tweetRepository.save(any(Tweet.class))).thenReturn(tweet);

        // Act
        Tweet savedTweet = tweetService.save(tweet, 1L);

        // Assert
        assertNotNull(savedTweet);
        assertEquals("Hello World", savedTweet.getContent());
        assertEquals(user, savedTweet.getUser());
    }

    @Test
    void findAllByUserId_ShouldReturnTweetList() {
        // Arrange
        when(tweetRepository.findByUserId(1L)).thenReturn(Arrays.asList(tweet));

        // Act
        List<Tweet> tweets = tweetService.findAllByUserId(1L);

        // Assert
        assertFalse(tweets.isEmpty());
        assertEquals(1, tweets.size());
    }

    @Test
    void delete_ShouldDeleteTweet_WhenUserIsOwner() {
        // Arrange
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(tweet));

        // Act
        tweetService.delete(1L, 1L);

        // Assert
        verify(tweetRepository).delete(tweet);
    }

    @Test
    void delete_ShouldThrowException_WhenUserIsNotOwner() {
        // Arrange
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(tweet));

        // Act & Assert
        // Başka bir kullanıcı (ID: 2) silmeye çalışıyor
        assertThrows(RuntimeException.class, () -> tweetService.delete(1L, 2L));
        verify(tweetRepository, never()).delete(any(Tweet.class));
    }
}
