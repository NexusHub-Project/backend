package com.nexushub.NexusHub.Riot.Match.dto.v4;

import com.nexushub.NexusHub.Riot.Match.dto.v3.ParticipantDto;
import com.nexushub.NexusHub.Riot.Match.dto.v3.ParticipantsResDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MinimalParticipantsResDto {
    private String puuid;
    private String gameName;
    private String tagLine;
    private Long championId;
    private String championNameEn;
    private String championNameKo;
    public static MinimalParticipantsResDto of (ParticipantDto participantResDto){
        return MinimalParticipantsResDto.builder()
                .puuid(participantResDto.getPuuid())
                .gameName(participantResDto.getGameName())
                .tagLine(participantResDto.getTagLine())
                .championId(participantResDto.getChampionId())
                .championNameEn(participantResDto.getChampionNameEn())
                .championNameKo(participantResDto.getChampionNameKo())
                .build();
    }
}
