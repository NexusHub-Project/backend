package com.nexushub.NexusHub.Exception.Normal;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CannotFoundPatchNote extends Exception {
    public CannotFoundPatchNote(String message) {
        super(message);
    }
}
