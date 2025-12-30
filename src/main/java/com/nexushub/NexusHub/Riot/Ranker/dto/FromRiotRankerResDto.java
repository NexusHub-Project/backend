package com.nexushub.NexusHub.Riot.Ranker.dto;

import com.nexushub.NexusHub.Riot.RiotInform.dto.Ranker.ChallengerDto;
import lombok.Data;

import java.util.List;

@Data
public class FromRiotRankerResDto {
    private String tier;
    private String leagueId;
    private String queue;
    private String name;
    private List<RiotRankerDto> entries;
}
