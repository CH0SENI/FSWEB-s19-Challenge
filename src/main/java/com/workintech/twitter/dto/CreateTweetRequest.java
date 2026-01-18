package com.workintech.twitter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateTweetRequest {
    
    @NotBlank(message = "Tweet content cannot be blank")
    @Size(max = 280, message = "Tweet content cannot exceed 280 characters")
    private String content;
}
