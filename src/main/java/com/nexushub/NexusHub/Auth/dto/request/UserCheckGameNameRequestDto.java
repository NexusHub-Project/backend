package com.nexushub.NexusHub.Auth.dto.request;

import lombok.Data;

@Data
public class UserCheckGameNameRequestDto {
    private String gameName;
    private String tagLine;
}
