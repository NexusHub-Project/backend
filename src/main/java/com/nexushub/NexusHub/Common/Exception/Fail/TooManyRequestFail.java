package com.nexushub.NexusHub.Common.Exception.Fail;

public class TooManyRequestFail extends RuntimeException {
    public TooManyRequestFail(String message) {
        super(message);
    }
}
