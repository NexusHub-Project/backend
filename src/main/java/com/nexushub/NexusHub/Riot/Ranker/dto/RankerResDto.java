package com.nexushub.NexusHub.Riot.Ranker.dto;

import com.nexushub.NexusHub.Riot.RiotInform.dto.Ranker.ChallengerDto;
import com.nexushub.NexusHub.Riot.Summoner.domain.Summoner;
import lombok.Builder;
import lombok.Getter;

// 챌, 그마, 마 모두 포괄적으로 사용할 수 있는 ResDTO
@Getter
@Builder
public class RankerResDto {
    private String puuid;
    private String gameName;
    private String tagLine;
    private Integer leaguePoints;
    private Integer wins;
    private Integer losses;
    private String rank;
    private Double winRate;


    public static RankerResDto of (Summoner summoner) {
        return RankerResDto.builder()
                .puuid(summoner.getPuuid())
                .gameName(summoner.getGameName())
                .tagLine(summoner.getTagLine())
                .leaguePoints(summoner.getSoloRankLP())
                .wins(summoner.getSoloRankWin())
                .losses(summoner.getSoloRankDefeat())
                .rank(summoner.getSoloRankTier())
                .winRate(((double) summoner.getSoloRankWin() / (summoner.getSoloRankDefeat() + summoner.getSoloRankWin())) * 100)
                .build();
    }
}
