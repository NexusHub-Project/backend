package com.nexushub.NexusHub.Auth.dto.request;

import lombok.Data;

@Data
public class UserSignUpRequestDto {
    private String loginId;
    private String loginPw;
    private String gameName;
    private String tagLine;
    private String puuid;
    // false
    private Boolean isPresentId;
    // true
    private Boolean isPresentGameName;
}
