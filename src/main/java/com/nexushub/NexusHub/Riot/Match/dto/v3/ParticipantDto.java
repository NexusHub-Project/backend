package com.nexushub.NexusHub.Riot.Match.dto.v3;

import com.nexushub.NexusHub.Riot.Match.domain.MatchParticipant;
import com.nexushub.NexusHub.Riot.Summoner.domain.Summoner;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParticipantDto {
    private String puuid;
    private String gameName;
    private String tagLine;
    private Long championId;

    public static ParticipantDto of(MatchParticipant participant) {
        Summoner summoner = participant.getSummoner();
        return ParticipantDto.builder()
                .puuid(summoner.getPuuid())
                .gameName(summoner.getGameName())
                .tagLine(summoner.getTagLine())
                .championId(participant.getChampionId())
                .build();
    }
}
