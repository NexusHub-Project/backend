package com.nexushub.NexusHub.Exception.RiotAPI;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IsPresentLoginId extends Exception {
    public IsPresentLoginId(String message) {
        super(message);
    }
}
