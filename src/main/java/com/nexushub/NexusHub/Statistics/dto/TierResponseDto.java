package com.nexushub.NexusHub.Statistics.dto;

import com.nexushub.NexusHub.Statistics.domain.Tier;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class TierResponseDto {
    private String championName;
    private Integer tier;
    private Integer score;
    private Float pickRate;
    private Float winRate;
    private Integer scoreDiff;

    public TierResponseDto(Tier tier) {
        this.tier = tier.getTier();
        this.score = tier.getScore();
        this.pickRate = tier.getPickRate();
        this.winRate = tier.getWinRate();
        this.scoreDiff = tier.getScoreDiff();
        this.championName = tier.getChampion().getNameKo();
    }
}
