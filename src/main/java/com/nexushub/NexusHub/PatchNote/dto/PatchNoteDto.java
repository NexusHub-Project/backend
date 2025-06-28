package com.nexushub.NexusHub.PatchNote.dto;

import com.nexushub.NexusHub.PatchNote.domain.PatchNote;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

public class PatchNoteDto {

    @Getter
    @NoArgsConstructor
    public static class CreateRequest {
        private String title;
        private String content;
    }

    @Getter
    public static class Response {
        private Long id;
        private String title;
        private String content;
        private String authorName;
        private LocalDateTime createdAt;

        public Response(PatchNote patchNote) {
            this.id = patchNote.getId();
            this.title = patchNote.getTitle();
            this.content = patchNote.getContent();
            this.authorName = patchNote.getAuthor().getGameName(); // 작성자 이름
            this.createdAt = patchNote.getCreatedAt();
        }
    }
}
