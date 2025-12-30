package com.nexushub.NexusHub.Riot.Ranker.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RiotRankerDto {
    private String puuid;
    private Integer leaguePoints;
    private Integer wins;
    private Integer losses;
    private String rank;

    // 이게 뭔지 정확하지는 않음
    private Boolean veteran;
    private Boolean inactive;
    private Boolean freshBlood;
    private Boolean hotStreak;
}
