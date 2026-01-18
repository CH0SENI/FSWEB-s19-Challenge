package com.workintech.twitter.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tweets", schema = "public")
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Tweet content cannot be blank")
    @Size(max = 280, message = "Tweet content cannot exceed 280 characters")
    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "tweet", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(mappedBy = "tweet", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Like> likes;

    @OneToMany(mappedBy = "tweet", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Retweet> retweets;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
