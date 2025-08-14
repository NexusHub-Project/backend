package com.nexushub.NexusHub.Riot.Summoner.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RankDto {
    private String rankTier;
    private Integer rankLP;
    private Integer rankWin;
    private Integer rankDefeat;
    private String rankType;

    public static RankDto of(String rankType, String rankTier, Integer rankLP, Integer rankWin, Integer rankDefeat) {
        return RankDto.builder()
                .rankDefeat(rankDefeat)
                .rankWin(rankWin)
                .rankTier(rankTier)
                .rankLP(rankLP)
                .rankType(rankType)
                .build();
    }
}
