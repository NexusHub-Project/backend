package com.nexushub.NexusHub.Riot.Ranker.controller;

import com.nexushub.NexusHub.Common.Exception.RiotAPI.CannotFoundSummoner;
import com.nexushub.NexusHub.Riot.Ranker.domain.Ranker;
import com.nexushub.NexusHub.Riot.Ranker.domain.Tier;
import com.nexushub.NexusHub.Riot.Ranker.dto.FromRiotRankerResDto;
import com.nexushub.NexusHub.Riot.Ranker.dto.RankerResDto;
import com.nexushub.NexusHub.Riot.RiotInform.dto.Ranker.ChallengerLeagueDto;
import com.nexushub.NexusHub.Riot.RiotInform.dto.Ranker.ChallengersResDto;
import com.nexushub.NexusHub.Riot.RiotInform.service.RiotApiService;
import com.nexushub.NexusHub.Riot.Summoner.domain.Summoner;
import com.nexushub.NexusHub.Riot.Summoner.service.SummonerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Queue;

@RestController
@RequestMapping("/api/v1/ranker")
@Slf4j
@RequiredArgsConstructor
public class RankerController {
    private final RiotApiService riotApiService;
    private final SummonerService summonerService;

    @GetMapping("/search/{tier}")
    public ResponseEntity<Queue<RankerResDto>> searchChallenger(@PathVariable String tier) throws CannotFoundSummoner {
        log.info("RankerController - /ranker/search/{} 호출 ", tier);
        Queue<RankerResDto> rankerResDtos = null;
        // 라이엇에서 챌린저 정보를 쫙 가져옴
        if (tier.equals("challenger")) {
            FromRiotRankerResDto challengers = riotApiService.getChallengersV2(Tier.CHALLENGER);
            rankerResDtos = summonerService.setRankersDataV2(challengers, Tier.CHALLENGER);
        }
        else if (tier.equals("grandmaster")){
            FromRiotRankerResDto grandmasters = riotApiService.getChallengersV2(Tier.GRANDMASTER);
            rankerResDtos = summonerService.setRankersDataV2(grandmasters, Tier.GRANDMASTER);
        }
        else if (tier.equals("master")) {
            FromRiotRankerResDto masters = riotApiService.getChallengersV2(Tier.MASTER);
            rankerResDtos = summonerService.setRankersDataV2(masters, Tier.MASTER);
        }
        return ResponseEntity.ok(rankerResDtos);
    }
}
