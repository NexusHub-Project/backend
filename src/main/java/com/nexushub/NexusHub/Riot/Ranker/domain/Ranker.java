package com.nexushub.NexusHub.Riot.Ranker.domain;

import com.nexushub.NexusHub.Riot.Summoner.domain.Summoner;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ranker {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    // Ranker 테이블 입장에서 외래키로 Summoner_id를 갖는다
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "summoner_id")
    private Summoner summoner;

    ///  티어 (챌, 그마, 마)
    @Column(nullable = false)
    private Tier tier;
    // 점수
    private Integer leaguePoint;
    // 랭킹 순위
    private Integer ranking;

    public void update(Tier tier,  Integer ranking, Integer lp){
        this.tier = tier;
        this.leaguePoint = lp;
        this.ranking = ranking;
    }

    public static Ranker of(Summoner summoner, Tier tier, Integer lp, Integer ranking){
        return Ranker.builder()
                .tier(tier)
                .ranking(ranking)
                .summoner(summoner)
                .leaguePoint(lp)
                .build();
    }
}
