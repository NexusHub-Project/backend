package com.nexushub.NexusHub.Statistics.dto.detail;

import com.nexushub.NexusHub.Statistics.domain.MatchUp.MatchUp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class ChampionDetailList {
    private List<ChampionDetailWith> champions;
    public ChampionDetailList(List<MatchUp> matchUps){
        this.champions = new LinkedList<>();
        for (MatchUp matchUp : matchUps) {
            champions.add(new ChampionDetailWith(matchUp.getOpponent().getNameKo(), matchUp.getOpponent().getNameEn(), matchUp.getGamesPlayed(), matchUp.getRelativeWinRate()));
        }
    }
}
