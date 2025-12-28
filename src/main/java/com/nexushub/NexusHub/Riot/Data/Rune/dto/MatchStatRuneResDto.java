package com.nexushub.NexusHub.Riot.Data.Rune.dto;

import com.nexushub.NexusHub.Riot.Match.dto.perks.PerksDto;
import com.nexushub.NexusHub.Riot.Match.dto.perks.StatPerksDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MatchStatRuneResDto {
    private int defense;
    private int flex;
    private int offense;

    public static MatchStatRuneResDto of (StatPerksDto statPerks){
        return MatchStatRuneResDto.builder()
                .flex(statPerks.getFlex())
                .offense(statPerks.getOffense())
                .defense(statPerks.getDefense())
                .build();
    }
}
