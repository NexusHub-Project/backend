package com.nexushub.NexusHub.Comment.repository;

import com.nexushub.NexusHub.Comment.domain.Comment;
import com.nexushub.NexusHub.PatchNote.domain.PatchNote;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findByPatchNote(PatchNote patchNote);
}
