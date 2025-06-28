package com.nexushub.NexusHub.Summoner.domain;


import com.nexushub.NexusHub.Summoner.dto.SummonerDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Summoner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_name", length = 100, nullable = false)
    private String gameName;

    @Column(name="tag_line", length = 20, nullable = false)
    private String tagLine;

    @Column(name = "puuid", length = 100, nullable = false, unique = true)
    private String puuid;

    // 솔랭 정보
    @Column(name = "solo_rank_tier", length = 20)
    private String soloRankTier;

    @Column(name = "solo_rank_lp")
    private Integer soloRankLP;

    @Column(name = "solo_rank_win")
    private Integer soloRankWin;

    @Column(name = "solo_rank_defeat")
    private Integer soloRankDefeat;

    // 자랭 정보
    @Column(name = "flexRankTier", length = 20)
    private String flexRankTier;

    @Column(name = "flexRankLP")
    private Integer flexRankLP;

    @Column(name = "flex_rank_win")
    private Integer flexRankWin;

    @Column(name = "flex_rank_defeat")
    private Integer flexRankDefeat;

    @Column(name = "create_at", updatable = false)
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }


    public static Summoner update(SummonerDto dto) {
        return Summoner.builder()
                .gameName(dto.getGameName())
                .tagLine(dto.getTagLine())
                .puuid(dto.getPuuid())
                .soloRankTier(dto.getSoloRankTier())
                .soloRankLP(dto.getSoloRankLP())
                .soloRankWin(dto.getSoloRankWin())
                .soloRankDefeat(dto.getSoloRankDefeat())
                .flexRankTier(dto.getFlexRankTier())
                .flexRankLP(dto.getFlexRankLP())
                .flexRankWin(dto.getFlexRankWin())
                .flexRankDefeat(dto.getFlexRankDefeat())
                .build();
    }
    public Summoner updateTier(SummonerDto dto){
        this.soloRankTier = dto.getSoloRankTier();
        this.soloRankLP = dto.getSoloRankLP();
        this.soloRankWin = dto.getSoloRankWin();
        this.soloRankDefeat = dto.getSoloRankDefeat();
        this.flexRankTier = dto.getFlexRankTier();
        this.flexRankLP = dto.getFlexRankLP();
        this.flexRankWin = dto.getFlexRankWin();
        this.flexRankDefeat = dto.getFlexRankDefeat();
        return this;
    }

}
