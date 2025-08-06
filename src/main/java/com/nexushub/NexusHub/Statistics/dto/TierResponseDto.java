package com.nexushub.NexusHub.Statistics.dto;

import com.nexushub.NexusHub.Statistics.domain.Champion.ChampionStatsByPosition;
import lombok.Data;

@Data
public class TierResponseDto {
    private String championName;
    private String championNameEn;
    private Integer tier;
    private Integer score;
    private Float pickRate;
    private Float winRate;
    private Integer scoreDiff;

    public TierResponseDto(ChampionStatsByPosition stats) {
        this.championName = stats.getChampion().getNameKo();
        this.championNameEn = stats.getChampion().getNameEn();
        this.tier = stats.getTier();
        this.score = stats.getScore();
        this.pickRate = stats.getPickRate();
        this.winRate = stats.getWinRate();
        this.scoreDiff = stats.getScore();
    }
}
