package com.nexushub.NexusHub.Riot.RiotInform.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileResDto {
    private Integer profileIconId;
    private Integer summonerLevel;
    private String profileUrl;

    public static ProfileResDto of(ProfileDto profileDto) {
        return ProfileResDto.builder()
                .profileIconId(profileDto.getProfileIconId())
                .summonerLevel(profileDto.getSummonerLevel())
                .profileUrl("https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/profile-icons/"+profileDto.getProfileIconId()+".jpg")
                .build();
    }
}
