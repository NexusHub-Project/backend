package com.nexushub.NexusHub.Web.Comment.repository;

import com.nexushub.NexusHub.Web.Comment.domain.Comment;
import com.nexushub.NexusHub.Web.Guide.domain.Guide;
import com.nexushub.NexusHub.Web.PatchNote.domain.PatchNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPatchNote(PatchNote patchNote);
    List<Comment> findByGuide(Guide guid);
}
