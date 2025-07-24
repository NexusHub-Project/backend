package com.nexushub.NexusHub.Statistics.dto.detail;

import com.nexushub.NexusHub.Match.dto.perks.PerksDto;
import com.nexushub.NexusHub.Statistics.domain.Champion.ChampionStatsByPosition;
import lombok.Builder;
import lombok.Data;

@Data
public class ChampionDetailBuild {
    // 어느 라인에 대한 빌드인가?
    private String lane;

    // 아이템 정보
    private Integer item01;
    private Integer item02;
    private Integer item03;
    private Integer item04;
    private Integer item05;
    private Integer item06;

    // 룬 정보
    private PerksDto perks;

    public ChampionDetailBuild(ChampionStatsByPosition stats) {
        this.lane = stats.getPosition().toString();
        this.perks = stats.getPerks();
        this.item01 = 3078; // 트포
        this.item02 = 3053; // 스태락
        this.item03 = 3153; // 몰왕
        this.item04 = 6333; // 죽무
        this.item05 = 3026; // 가엔
        this.item06 = 2055; // 제어와드

    }
}
