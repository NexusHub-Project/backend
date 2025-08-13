package com.nexushub.NexusHub.Riot.Summoner.dto;

import lombok.Data;

@Data
public class SummonerRequestDto {
    private Long summonerId;
    private String gameName;
    private String tagLine;
    private String puuid;

    public SummonerRequestDto(String gameName, String tagLine) {
        this.tagLine = tagLine;
        this.gameName = gameName;
    }
}
