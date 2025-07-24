package com.nexushub.NexusHub.Champion.dto.detail;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChampionDetailWith {
    private String championName;
    private Integer gameCount;
    private Float winRate;
}
