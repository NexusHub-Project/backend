package com.nexushub.NexusHub.Comment.service;

import com.nexushub.NexusHub.Comment.domain.Comment;
import com.nexushub.NexusHub.Comment.dto.CommentDto;
import com.nexushub.NexusHub.Comment.repository.CommentRepository;
import com.nexushub.NexusHub.Exception.Fail.DeleteFail;
import com.nexushub.NexusHub.Exception.Fail.EditFail;
import com.nexushub.NexusHub.Exception.Normal.CannotFoundComment;
import com.nexushub.NexusHub.Guide.domain.Guide;
import com.nexushub.NexusHub.PatchNote.domain.PatchNote;
import com.nexushub.NexusHub.User.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Comment savePatchNoteComment(CommentDto.WriteRequest requestDto, User author, PatchNote patchNote) {
        Comment comment = new Comment(requestDto.getContent(), author, patchNote);

        return commentRepository.save(comment);
    }
    public Comment saveGuideComment(CommentDto.WriteRequest requestDto, User author, Guide guide){
        Comment comment = new Comment(requestDto.getContent(), author, guide);

        return commentRepository.save(comment);
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new CannotFoundComment("해당 댓글을 찾을 수 없습니다."));
    }

    public Comment updateComment(CommentDto.EditRequest requestDto, User author) {
        // 1) 작성자인지 체크하기
        Comment comment = findById(requestDto.getCommentId());
        if (isAuthor(comment, author)) {
            comment.update(requestDto.getContent());
        }
        else {
            throw new EditFail("댓글 수정은 작성자만 가능합니다.");
        }
        return comment;
    }
    public boolean deleteComment(Long id, User author){
        Comment comment = findById(id);
        if (isAuthor(comment, author)) {
            commentRepository.delete(comment);
            return true;
        }
        else {
            throw new DeleteFail("댓글 삭제는 작성자만 가능합니다.");
        }
    }
    public void addLikeById(Long id){
        Comment comment = findById(id);
        comment.like();
    }
    public void addDislikeById(Long id){
        Comment comment = findById(id);
        comment.dislike();
    }
    public List<Comment> findPatchNoteCommentAll(PatchNote patchNote){
        return commentRepository.findByPatchNote(patchNote);
    }

    public List<Comment> findGuideCommentAll(Guide guide){
        return commentRepository.findByGuide(guide);
    }


    private boolean isAuthor(Comment comment, User author){
        return comment.getAuthor().equals(author);
    }
}
