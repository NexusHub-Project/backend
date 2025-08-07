package com.nexushub.NexusHub.Riot.Match.domain;

import com.nexushub.NexusHub.Riot.Match.dto.perks.PerksDto;
import com.nexushub.NexusHub.Riot.Match.dto.perks.PerksDtoConverter;
import com.nexushub.NexusHub.Riot.Summoner.domain.Summoner;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어느 Match에 속한 건지?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    // 어느 Summoner인지?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "summoner_id")
    private Summoner summoner;

    private Boolean win;
    private Long championId;
    private Integer champLevel;
    private String teamPosition;
    private Long item0, item1, item2, item3, item4, item5, item6;

    // 킬뎃 정보
    private Float kda;
    private Integer kills;
    private Integer deaths;
    private Integer assists;

    private Long totalDamageDealtToChampions;
    private Long totalDamageTaken;

    // 미니언 처치 정보
    private Integer totalMinionKills;

    // 연속킬 정보
    private Integer doubleKills;
    private Integer tripleKills;
    private Integer quadraKills;
    private Integer pentaKills;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = PerksDtoConverter.class)
    private PerksDto perks;

    // 지금은 분석 툴이 없으니 100~20 사이의 랜덤 점수 부여 (자체 분석 점수, 팀운 점수)
    private Integer teamLuckScore;
    private Integer ourScore;


    public void setTeamLuckScore(Integer score) {
        this.teamLuckScore = score;
    }
    public void setOurScore(Integer score) {
        this.ourScore = score;
    }

}
