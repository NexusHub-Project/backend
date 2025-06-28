package com.nexushub.NexusHub.Exception.RiotAPI;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CannotFindSummoner extends Exception {
    public CannotFindSummoner(String message) {
        super(message);
    }
}
