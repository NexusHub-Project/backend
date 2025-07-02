package com.nexushub.NexusHub.Comment.controller;

import com.nexushub.NexusHub.Comment.domain.Comment;
import com.nexushub.NexusHub.Comment.dto.CommentDto;
import com.nexushub.NexusHub.Comment.repository.CommentRepository;
import com.nexushub.NexusHub.Comment.service.CommentService;
import com.nexushub.NexusHub.Exception.Normal.CannotFoundGuide;
import com.nexushub.NexusHub.Exception.Normal.CannotFoundPatchNote;
import com.nexushub.NexusHub.Exception.Normal.CannotFoundUser;
import com.nexushub.NexusHub.Guide.domain.Guide;
import com.nexushub.NexusHub.Guide.service.GuideService;
import com.nexushub.NexusHub.PatchNote.domain.PatchNote;
import com.nexushub.NexusHub.PatchNote.service.PatchNoteService;
import com.nexushub.NexusHub.User.domain.User;
import com.nexushub.NexusHub.User.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;
    private final PatchNoteService patchNoteService;
    private final GuideService guideService;
    private final CommentRepository commentRepository;

    // 댓글 쓰기 (패치노트)
    @PostMapping("/patchnote/{patch_note_id}/write")
    public ResponseEntity<?> writeNote(
            @PathVariable("patch_note_id") Long id,
            @AuthenticationPrincipal String loginId,
            @RequestBody CommentDto.Request requestDto) throws CannotFoundUser, CannotFoundPatchNote {
        // 1) 토큰을 통해서 가져온 아이디를 통해서 작성자 객체를 가져온다

        User author = userService.findByLoginId(loginId)
                .orElseThrow(() -> new CannotFoundUser("해당 유저 정보를 찾을 수 없습니다."));


        // 2) id를 통해서 patchNote 객체를 찾아옴
        PatchNote patchNote = patchNoteService.findById(id)
                .orElseThrow(() -> new CannotFoundPatchNote("해당 패치노트 글을 찾을 수 없습니다"));


        // 3) Service로 내용과 author를 넘겨줌
        Comment comment = commentService.savePatchNoteComment(requestDto, author, patchNote);

        return ResponseEntity.ok(CommentDto.Response.of(comment, author));
    }

    @PostMapping("/guide/{guide_id}/write")
    public ResponseEntity<?> writeGuideComment(
            @PathVariable("guide_id") Long id,
            @AuthenticationPrincipal String loginId,
            @RequestBody CommentDto.Request requestDto) throws CannotFoundUser, CannotFoundGuide {
        User author = userService.findByLoginId(loginId)
                .orElseThrow(() -> new CannotFoundUser("해당 유저의 정보를 찾을 수 없습니다."));

        Guide guide = guideService.findById(id)
                .orElseThrow(() -> new CannotFoundGuide("해당 공략 정보를 찾을 수 없습니다."));

        Comment comment = commentService.saveGuideComment(requestDto, author, guide);

        return ResponseEntity.ok(CommentDto.Response.of(comment, author));
    }


    // 댓글 수정
    @PatchMapping("/{comment_id}/edit")
    public ResponseEntity<?> editComment(
            @PathVariable Long comment_id,
            @AuthenticationPrincipal String loginId,
            @RequestBody CommentDto.Request requestDto) throws CannotFoundUser {

        // 토큰을 통해서 author 객체를 받아서 service로 넘겨줌
        User author = userService.findByLoginId(loginId)
                .orElseThrow(() -> new CannotFoundUser("해당 유저 정보를 찾을 수 없습니다."));
        requestDto.setCommentId(comment_id);
        Comment comment = commentService.updateComment(requestDto, author);

        return ResponseEntity.ok(CommentDto.Response.of(comment, author));
    }

    // 댓글 삭제
    @DeleteMapping("/{comment_id}/delete")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long comment_id,
            @AuthenticationPrincipal String loginId) throws CannotFoundUser {

        // 토큰을 통해서 author 객체를 받아서 service로 넘겨줌
        User author = userService.findByLoginId(loginId)
                .orElseThrow(() -> new CannotFoundUser("해당 유저 정보를 찾을 수 없습니다."));

        commentService.deleteComment(comment_id, author);

        return ResponseEntity.ok("삭제 하였습니다.");
    }

    @PostMapping("/show/{id}/likes")
    public ResponseEntity<?> like(@PathVariable Long id) {
        commentService.addLikeById(id);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "댓글에 좋아요를 눌렀습니다.");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PostMapping("/show/{id}/dislikes")
    public ResponseEntity<?> dislike(@PathVariable Long id) {
        commentService.addDislikeById(id);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "댓글에 싫어요를 눌렀습니다.");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @GetMapping("/{id}/{type}")
    public ResponseEntity<?> getComment(@PathVariable Long id, @PathVariable String type) throws CannotFoundPatchNote, CannotFoundGuide {
        List<Comment> comments;
        List<CommentDto.PostResponseDto> commentDtoResponses = new ArrayList<>();

        if (type.equals("patchNote")) {
            PatchNote patchNote = patchNoteService.findById(id)
                    .orElseThrow(() -> new CannotFoundPatchNote("해당 패치노트 글을 찾을 수 없습니다"));
            comments = commentService.findPatchNoteCommentAll(patchNote);

            for (Comment comment : comments) {
                commentDtoResponses.add(CommentDto.PostResponseDto.of(comment, comment.getAuthor()));
            }
        }
        else if (type.equals("guide")) {
            Guide guide = guideService.findById(id)
                    .orElseThrow(() -> new CannotFoundGuide("해당 공략 글을 찾을 수 없습니다."));

            comments = commentService.findGuideCommentAll(guide);
            for (Comment comment : comments) {
                commentDtoResponses.add(CommentDto.PostResponseDto.of(comment, comment.getAuthor()));
            }
        }

        return ResponseEntity.ok(commentDtoResponses);
    }

}
