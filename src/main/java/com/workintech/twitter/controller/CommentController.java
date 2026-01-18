package com.workintech.twitter.controller;

import com.workintech.twitter.dto.CreateCommentRequest;
import com.workintech.twitter.entity.Comment;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.service.CommentService;
import com.workintech.twitter.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController extends BaseController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService, UserService userService) {
        super(userService);
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(@Valid @RequestBody CreateCommentRequest request, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        
        Comment savedComment = commentService.save(comment, user.getId(), request.getTweetId());
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Comment updateComment(@PathVariable Long id, @Valid @RequestBody CreateCommentRequest request, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        return commentService.update(id, comment, user.getId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        commentService.delete(id, user.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
