package com.nexushub.NexusHub.Riot.Match.dto.v3;

import com.nexushub.NexusHub.Riot.Match.domain.MatchParticipant;
import com.nexushub.NexusHub.Riot.Match.dto.perks.PerksDto;
import com.nexushub.NexusHub.Riot.Match.dto.perks.StyleDto;
import com.nexushub.NexusHub.Riot.Summoner.domain.Summoner;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MyDataResDto {

    private String puuid;
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
    private Integer teamLuckScore;
    private Integer ourScore;

    private Integer primaryStyle;
    private Integer subStyle;

    public static MyDataResDto of(MatchParticipant participants) {
        Summoner summoner = participants.getSummoner();
        return MyDataResDto.builder()
                .puuid(summoner.getPuuid())
                .win(participants.getWin())
                .championId(participants.getChampionId())
                .champLevel(participants.getChampLevel())
                .teamPosition(participants.getTeamPosition())
                .item0(participants.getItem0()).item1(participants.getItem1()).item2(participants.getItem2())
                .item3(participants.getItem3()).item4(participants.getItem4()).item5(participants.getItem5())
                .item6(participants.getItem6())

                .kda(participants.getKda())
                .kills(participants.getKills())
                .deaths(participants.getDeaths())
                .assists(participants.getAssists())
                .totalDamageDealtToChampions(participants.getTotalDamageDealtToChampions())
                .totalDamageTaken(participants.getTotalDamageTaken())
                .totalMinionKills(participants.getTotalMinionKills())
                .doubleKills(participants.getDoubleKills())
                .tripleKills(participants.getTripleKills())
                .quadraKills(participants.getQuadraKills())
                .pentaKills(participants.getPentaKills())
                .teamLuckScore((int)Math.random()*45+40)
                .ourScore((int)Math.random()*45+40)
                .build();
    }

    public void setPerks(MatchParticipant participants) {
        Summoner summoner = participants.getSummoner();
        List<StyleDto> styles = participants.getPerks().getStyles();
        for (StyleDto style : styles) {
            if (style.getDescription().equals("primaryStyle")) {
                this.primaryStyle = style.getStyle();
            }
            else if (style.getDescription().equals("subStyle")) {
                this.subStyle = style.getStyle();
            }
        }
    }
}
