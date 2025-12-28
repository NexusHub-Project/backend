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
    private Long pathId;
    private String pathName;
    private String pathIcon;
    private String pathRuneKey;


    public RuneResponseDto(Rune rune) {
        this.id = rune.getId();
        this.name = rune.getName();
        this.key = rune.getRuneKey();
        this.icon = rune.getIcon();
        this.description = rune.getShortDesc();
        RunePath runePath = rune.getSlot().getRunePath();
        this.setPath(runePath);
    }

    private void setPath(RunePath path){
        this.pathId = path.getId();
        this.pathName = path.getName();
        this.pathIcon = path.getIcon();
        this.pathRuneKey = path.getRuneKey();
    }
}