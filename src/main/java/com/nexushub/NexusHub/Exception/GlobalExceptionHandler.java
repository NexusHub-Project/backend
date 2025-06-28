package com.nexushub.NexusHub.Exception;

import com.nexushub.NexusHub.Exception.RiotAPI.CannotFindSummoner;
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

    @ExceptionHandler(CannotFindSummoner.class)
    public ResponseEntity<?> handleCannotFindSummoner(CannotFindSummoner e) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
