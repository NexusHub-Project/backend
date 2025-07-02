package com.nexushub.NexusHub.Comment.repository;

import com.nexushub.NexusHub.Comment.domain.Comment;
import com.nexushub.NexusHub.Guide.domain.Guide;
import com.nexushub.NexusHub.PatchNote.domain.PatchNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPatchNote(PatchNote patchNote);
    List<Comment> findByGuide(Guide guid);
}
