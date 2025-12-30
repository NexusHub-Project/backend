package com.nexushub.NexusHub.Riot.Ranker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RiotRankerDto {
    // [중요] API에서는 "summonerId"로 내려오므로 매핑 명시
    // 이 값은 실제 PUUID가 아니라 Encrypted Summoner ID입니다.
    @JsonProperty("summonerId")
    private String puuid;

    private Integer leaguePoints;
    private Integer wins;
    private Integer losses;
    private String rank;

    private Boolean veteran;
    private Boolean inactive;
    private Boolean freshBlood;
    private Boolean hotStreak;
}