package com.nexushub.NexusHub.Statistics.dto;

import com.nexushub.NexusHub.InGame.Champion.Champion;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChampionNameResDto {
    private String championName;
    private String championNameEn;

    public static ChampionNameResDto of(Champion champion) {
        return ChampionNameResDto.builder()
                .championName(champion.getNameKo())
                .championNameEn(champion.getNameEn())
                .build();
    }
}
