package com.nexushub.NexusHub.Exception.Handler;

import com.nexushub.NexusHub.Exception.Fail.DeleteFail;
import com.nexushub.NexusHub.Exception.Fail.EditFail;
import com.nexushub.NexusHub.Exception.Normal.CannotFoundComment;
import com.nexushub.NexusHub.Exception.Normal.CannotFoundPatchNote;
import com.nexushub.NexusHub.Exception.RiotAPI.CannotFoundSummoner;
import com.nexushub.NexusHub.Exception.Normal.CannotFoundUser;
import com.nexushub.NexusHub.Exception.RiotAPI.IsPresentLoginId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IsPresentLoginId.class)
    public ResponseEntity<?> handleIsPresentLoginId(IsPresentLoginId e) {
        Map<String, Object> error = new HashMap<>();
        error.put("enroll", "fail");
        error.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(CannotFoundSummoner.class)
    public ResponseEntity<?> handleCannotFindSummoner(CannotFoundSummoner e) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(CannotFoundUser.class)
    public ResponseEntity<?> handleCannotFindUser(CannotFoundUser e) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(CannotFoundPatchNote.class)
    public ResponseEntity<?> handleCannotFindPatchNote(CannotFoundPatchNote e) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(CannotFoundComment.class)
    public ResponseEntity<?> handleCannotFindComment(CannotFoundComment e) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(EditFail.class)
    public ResponseEntity<?> editFailComment(EditFail e) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(DeleteFail.class)
    public ResponseEntity<?> deleteFailComment(DeleteFail e) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }
}
