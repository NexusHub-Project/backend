package com.nexushub.NexusHub.Riot.dto.Ranker;

import com.nexushub.NexusHub.Summoner.domain.Summoner;
import lombok.Builder;
import lombok.Data;

@Data
public class ChallengersResponseDto {
    private String puuid;
    private String gameName;
    private String tagLine;
    private Integer leaguePoints;
    private Integer wins;
    private Integer losses;
    private String rank;
    private Double winRate;

    @Builder
    public ChallengersResponseDto(ChallengerDto dto, Summoner summoner) {
        this.gameName = summoner.getGameName();
        this.tagLine = summoner.getTagLine();
        this.puuid = summoner.getPuuid();

        this.rank = dto.getRank();
        this.wins = dto.getWins();
        this.losses = dto.getLosses();

        this.winRate = (double) (wins / (losses + wins))*100;
    }
}
