package com.nexushub.NexusHub.Statistics.dto.detail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ChampionDetailWith {
    private String championName;
    private Integer gameCount;
    private Float winRate;
}
