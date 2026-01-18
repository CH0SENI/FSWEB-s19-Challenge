package com.workintech.twitter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCommentRequest {
    
    @NotBlank(message = "Comment content cannot be blank")
    private String content;
    
    @NotNull(message = "Tweet ID cannot be null")
    private Long tweetId;
}
