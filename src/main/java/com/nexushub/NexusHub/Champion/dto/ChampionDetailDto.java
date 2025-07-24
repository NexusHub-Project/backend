package com.nexushub.NexusHub.Champion.dto;

import com.nexushub.NexusHub.Champion.dto.detail.ChampionDetailBuild;
import com.nexushub.NexusHub.Champion.dto.detail.ChampionDetailChampInfo;
import com.nexushub.NexusHub.Champion.dto.detail.ChampionDetailList;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChampionDetailDto {
    private ChampionDetailChampInfo detailChampInfo;
    private ChampionDetailBuild detailChampBuild;
    private ChampionDetailList easyToDeal;
    private ChampionDetailList difficultToDeal;
    private ChampionDetailList synergyChampion;
}
