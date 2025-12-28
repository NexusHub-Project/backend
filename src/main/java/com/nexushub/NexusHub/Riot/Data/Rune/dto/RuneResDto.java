package com.nexushub.NexusHub.Riot.Data.Rune.dto;

import com.nexushub.NexusHub.Riot.Match.dto.perks.SelectionDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class RuneResDto {
    private int id;
    private String desc;

    public static RuneResDto of (SelectionDto dto){
        return RuneResDto.builder()
                .id(dto.getPerk())
                .build();
    }
}
