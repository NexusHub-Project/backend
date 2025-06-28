package com.nexushub.NexusHub.Auth.dto.request;

import lombok.Data;

@Data
public class UserLoginRequestDto {
    private String loginId;
    private String loginPw;
}
