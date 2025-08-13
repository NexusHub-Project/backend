package com.nexushub.NexusHub.Riot.Summoner.dto.v2;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nexushub.NexusHub.Riot.Summoner.controller.v1.SummonerController;
import com.nexushub.NexusHub.Riot.Summoner.domain.Summoner;
import lombok.Builder;
import lombok.Data;



@Data
@Builder
public class SummonerTierResDto {
    private Long id;
    private String gameName;
    private String tagLine;
    private String puuid;

    private RankDto soloRank;
    private RankDto flexRank;

    public static SummonerTierResDto of(Summoner summoner){
        return SummonerTierResDto.builder()
                .id(summoner.getId())
                .gameName(summoner.getGameName())
                .tagLine(summoner.getTagLine())
                .puuid(summoner.getPuuid())
                .soloRank(RankDto.of(
                        "SOLO",
                        summoner.getSoloRankTier(),
                        summoner.getSoloRankLP(),
                        summoner.getSoloRankWin(),
                        summoner.getSoloRankDefeat()))
                .flexRank(RankDto.of(
                        "FLEX",
                        summoner.getFlexRankTier(),
                        summoner.getFlexRankLP(),
                        summoner.getFlexRankWin(),
                        summoner.getFlexRankDefeat()))
                .build();
    }
}
