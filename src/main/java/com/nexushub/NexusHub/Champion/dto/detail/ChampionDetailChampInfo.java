package com.nexushub.NexusHub.Champion.dto.detail;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChampionDetailChampInfo {
    // 티어 -> 몇 티어인지?
    private Integer tier;
    // 순위 -> 몇 등인지? 동일 라인의 챔피언들에 비해서
    private Integer rank;
    // 승률
    private Float winRate;
    // 픽률
    private Float pickRate;
    // 벤률
    private Float banRate;
    // 게임 수
    private Integer gameCount;
}
