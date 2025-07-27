package com.nexushub.NexusHub.Summoner.dto;

import com.nexushub.NexusHub.Summoner.domain.Summoner;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SummonerResponseDto {
    // 정보
    private Long summonerId;
    private String gameName;
    private String tagLine;
    private String puuid;

    // 솔랭 정보
    private String soloRankTier;
    private Integer soloRankLP;
    private Integer soloRankWin;
    private Integer soloRankDefeat;


    // 자랭 정보
    private String flexRankTier;
    private Integer flexRankLP;
    private Integer flexRankWin;
    private Integer flexRankDefeat;

    // 매치 정보




    // 마지막 업데이트 시간
    private LocalDateTime updateAt;
    @Builder
    public SummonerResponseDto(Summoner summoner){
        this.gameName = summoner.getGameName();
        this.summonerId = summoner.getId();
        this.tagLine = summoner.getTagLine();
        this.puuid = summoner.getPuuid();

        this.soloRankDefeat = summoner.getFlexRankDefeat();
        this.soloRankWin = summoner.getFlexRankWin();
        this.soloRankTier = summoner.getSoloRankTier();
        this.soloRankLP = summoner.getSoloRankLP();

        this.flexRankDefeat = summoner.getFlexRankDefeat();
        this.flexRankWin = summoner.getFlexRankWin();
        this.flexRankTier = summoner.getFlexRankTier();
        this.flexRankLP = summoner.getFlexRankLP();

        this.updateAt = summoner.getUpdatedAt();
    }

}
