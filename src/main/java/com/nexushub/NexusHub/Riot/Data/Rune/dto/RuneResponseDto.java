package com.nexushub.NexusHub.Riot.Data.Rune.dto;

import com.nexushub.NexusHub.Riot.Data.Rune.domain.Rune;
import lombok.Getter;

@Getter
public class RuneResponseDto {
    private Long id;
    private String runeKey;
    private String defaultUrl = "https://ddragon.leagueoflegends.com/cdn/img/";
    private String name;
    private String icon;
    private String shortDesc;
    public RuneResponseDto(Rune rune){
        this.id = rune.getId();
        this.name = rune.getName();
        this.icon = defaultUrl + rune.getIcon();
        this.shortDesc = rune.getShortDesc();
    }
}
