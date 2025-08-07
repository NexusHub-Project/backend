package com.nexushub.NexusHub.Web.Guide.dto;

// patchNote 그대로 베낌

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import com.nexushub.NexusHub.Web.Guide.domain.Guide;

public class GuideDto {
    @Getter
    @NoArgsConstructor
    public static class GuideRequest {
        private String title;
        private String content;
        private Long championId;
    }

    @Getter
    public static class GuideResponseDto {
        private Long id;
        private String title;
        private String content;
        private String authorName;
        private Integer views;
        private Integer likes;
        private Integer dislikes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public GuideResponseDto(Guide guide) {
            this.id = guide.getId();
            this.title = guide.getTitle();
            this.content = guide.getContent();
            this.authorName = guide.getAuthor().getNickName(); // 작성자 이름
            this.createdAt = guide.getCreatedAt();
            this.updatedAt = guide.getUpdatedAt();
            this.views = guide.getViews();
            this.likes = guide.getLikes();
            this.dislikes = guide.getDislikes();
        }
    }

    @Getter
    public static class GuideUploadResponseDto {
        private Long id;
        private String title;
        private String authorName;
        private Long authorId;

        public GuideUploadResponseDto(Guide guide) {
            this.id = guide.getId();
            this.title = guide.getTitle();
            this.authorName = guide.getAuthor().getNickName();
            this.authorId = guide.getAuthor().getId();
        }
    }

    @Getter
    public static class GuideListResponseDto {
        private Long id;
        private String title;
        private String authorName;
        private Long authorId;
        private Integer views;
        private LocalDateTime createdAt;

        public GuideListResponseDto(Guide guide) {
            this.id = guide.getId();
            this.title = guide.getTitle();
            this.authorName = guide.getAuthor().getNickName();
            this.authorId = guide.getAuthor().getId();
            this.views = guide.getViews();
            this.createdAt = guide.getCreatedAt();
        }
    }


}
