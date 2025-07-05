package com.nexushub.NexusHub.Comment.dto;

import com.nexushub.NexusHub.Comment.domain.Comment;
import com.nexushub.NexusHub.User.domain.Role;
import com.nexushub.NexusHub.User.domain.User;
import lombok.*;

import java.time.LocalDateTime;

public class CommentDto {
    @Getter
    @Setter
    public static class Request{
        private String content;
        private Long commentId;
    }


    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response{
        private String authorName;
        private Long id;
        private String content;
        private LocalDateTime createdAt;
        private Role role;

        public static Response of(Comment comment, User author){
            return Response.builder()
                    .authorName(author.getGameName())
                    .id(comment.getId())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt())
                    .role(author.getRole())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class PostResponseDto {
        private String authorName;
        private Long id;
        private String content;
        private LocalDateTime createdAt;
        private Role role;
        private Integer likes;
        private Integer dislikes;

        public static PostResponseDto of(Comment comment, User author) {
            return PostResponseDto.builder()
                    .authorName(comment.getAuthor().getGameName())
                    .id(comment.getId())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt())
                    .role(comment.getAuthor().getRole())
                    .likes(comment.getLikes())
                    .dislikes(comment.getDislikes())
                    .build();
        }
    }
}
