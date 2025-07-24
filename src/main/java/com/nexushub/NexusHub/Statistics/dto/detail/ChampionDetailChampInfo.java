package com.nexushub.NexusHub.Statistics.dto.detail;

import com.nexushub.NexusHub.Statistics.domain.Champion.ChampionStatsByPosition;
import com.sun.jdi.FloatType;
import lombok.Builder;
import lombok.Data;

@Data
public class ChampionDetailChampInfo {
    // 티어 -> 몇 티어인지?
    private Integer tier;
    // 승률
    private Float winRate;
    // 픽률
    private Float pickRate;
    // 벤률
    private Float banRate;
    // 게임 수
    private Integer gameCount;
    // 비율
    private Float percent = (float) 0.0;

    public ChampionDetailChampInfo(ChampionStatsByPosition stats) {
        this.tier = stats.getTier();
        this.winRate = stats.getWinRate();
        this.pickRate = stats.getPickRate();
        this.banRate = stats.getBanRate();
        this.gameCount = stats.getTotalGamesPlayed();
    }
}
