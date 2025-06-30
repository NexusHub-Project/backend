package com.nexushub.NexusHub.Guide.domain;

import com.nexushub.NexusHub.User.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Guide {

    private Long id;
    private String title;
    private String content;
    private User author;
    private int views;
    private int likes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public Guide(Long id, String title, String content, User author,
                 int views, int likes,
                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.views = views;
        this.likes = likes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}