package com.nexushub.NexusHub.Riot.Ranker.dto;

import com.nexushub.NexusHub.Riot.Ranker.domain.Ranker;
import com.nexushub.NexusHub.Riot.Summoner.domain.Summoner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedisRankerDto {
    private String puuid;
    private String gameName;
    private String tagLine;
    private String tier;
    private int lp;
    private int wins;
    private int losses;

    //  레벨이랑 아이콘은 잠정 중단
    private int level;
    private int icon;

    public static RedisRankerDto of (Ranker ranker){
        Summoner summoner = ranker.getSummoner();
        return RedisRankerDto.builder()
                .puuid(summoner.getPuuid())
                .gameName(summoner.getGameName())
                .tagLine(summoner.getTagLine())
                .tier(summoner.getSoloRankTier())
                .lp(summoner.getSoloRankLP())
                .wins(summoner.getSoloRankWin())
                .losses(summoner.getSoloRankDefeat())
                .build();
    }
    public static RedisRankerDto of (Summoner summoner){
        return RedisRankerDto.builder()
                .puuid(summoner.getPuuid())
                .gameName(summoner.getGameName())
                .tagLine(summoner.getTagLine())
                .tier(summoner.getSoloRankTier())
                .lp(summoner.getSoloRankLP())
                .wins(summoner.getSoloRankWin())
                .losses(summoner.getSoloRankDefeat())
                .icon(summoner.getIconId())
                .level(summoner.getLevel())
                .build();
    }
}
