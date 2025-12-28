package com.nexushub.NexusHub.Riot.Data.Rune.dto;

import com.nexushub.NexusHub.Riot.Data.Rune.domain.Rune;
import com.nexushub.NexusHub.Riot.Data.Rune.domain.RunePath;
import com.nexushub.NexusHub.Riot.Data.Rune.domain.RuneSlot;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RuneResponseDto {
    private Long id;
    private String name;
    private String key;
    private String icon;
    private String description;



    public RuneResponseDto(Rune rune) {
        this.id = rune.getId();
        this.name = rune.getName();
        this.key = rune.getRuneKey();
        this.icon = rune.getIcon();
        this.description = rune.getShortDesc();
    }
    public RuneResponseDto(RunePath runePath){
        this.id = runePath.getId();
        this.name = runePath.getName();
        this.key = runePath.getRuneKey();
        this.icon = runePath.getIcon();
    }
}