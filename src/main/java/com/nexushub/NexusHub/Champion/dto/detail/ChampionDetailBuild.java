package com.nexushub.NexusHub.Champion.dto.detail;

import com.nexushub.NexusHub.Match.dto.perks.PerksDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChampionDetailBuild {
    // 어느 라인에 대한 빌드인가?
    private String line;

    // 해당 빌드의 비중이 어느정도가 되는지?
    private Integer percent;

    // 아이템 정보
    private Integer item01;
    private Integer item02;
    private Integer item03;
    private Integer item04;

    // 룬 정보
    PerksDto perks;
}
