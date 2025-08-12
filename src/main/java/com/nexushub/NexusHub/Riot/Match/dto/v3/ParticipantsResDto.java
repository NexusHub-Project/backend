package com.nexushub.NexusHub.Riot.Match.dto.v3;

import com.nexushub.NexusHub.Riot.Match.domain.MatchParticipant;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ParticipantsResDto {
    private ParticipantDto player0;
    private ParticipantDto player1;
    private ParticipantDto player2;
    private ParticipantDto player3;
    private ParticipantDto player4;

    private ParticipantDto player5;
    private ParticipantDto player6;
    private ParticipantDto player7;
    private ParticipantDto player8;
    private ParticipantDto player9;

    public static ParticipantsResDto of(List<MatchParticipant> participants) {
        return ParticipantsResDto.builder()
                .player0(ParticipantDto.of(participants.get(0)))
                .player1(ParticipantDto.of(participants.get(1)))
                .player2(ParticipantDto.of(participants.get(2)))
                .player3(ParticipantDto.of(participants.get(3)))
                .player4(ParticipantDto.of(participants.get(4)))
                .player5(ParticipantDto.of(participants.get(5)))
                .player6(ParticipantDto.of(participants.get(6)))
                .player7(ParticipantDto.of(participants.get(7)))
                .player8(ParticipantDto.of(participants.get(8)))
                .player9(ParticipantDto.of(participants.get(9)))
                .build();
    }
}
