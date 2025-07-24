package com.nexushub.NexusHub.Champion.dto.detail;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChampionDetailList {
    private List<ChampionDetailWith> champions;
}
