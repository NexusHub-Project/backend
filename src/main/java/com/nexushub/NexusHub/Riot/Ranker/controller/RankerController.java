package com.nexushub.NexusHub.Riot.Ranker.controller;

import com.nexushub.NexusHub.Common.Exception.RiotAPI.CannotFoundSummoner;
import com.nexushub.NexusHub.Riot.Ranker.domain.Ranker;
import com.nexushub.NexusHub.Riot.Ranker.domain.Tier;
import com.nexushub.NexusHub.Riot.Ranker.dto.FromRiotRankerResDto;
import com.nexushub.NexusHub.Riot.Ranker.dto.RankerResDto;
import com.nexushub.NexusHub.Riot.Ranker.service.RankerService;
import com.nexushub.NexusHub.Riot.RiotInform.dto.Ranker.ChallengerLeagueDto;
import com.nexushub.NexusHub.Riot.RiotInform.dto.Ranker.ChallengersResDto;
import com.nexushub.NexusHub.Riot.RiotInform.service.RiotApiService;
import com.nexushub.NexusHub.Riot.Summoner.domain.Summoner;
import com.nexushub.NexusHub.Riot.Summoner.service.SummonerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Queue;

@RestController
@RequestMapping("/api/v1/ranker")
@Slf4j
@RequiredArgsConstructor
public class RankerController {
    private final RiotApiService riotApiService;
    private final SummonerService summonerService;
    private final RankerService rankerService;

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
    @PostMapping("/refresh")
    @Operation(summary = "랭커 데이터 수동 갱신", description = "Riot API를 호출하여 챌린저/그마/마스터 데이터를 DB에 최신화합니다. (시간이 오래 걸릴 수 있음)")
    public ResponseEntity<String> refreshRankerData() {
        log.info("API 요청으로 랭커 데이터 갱신을 시작합니다.");

        // 서비스 메소드 호출
        rankerService.refreshRankerData();

        return ResponseEntity.ok("랭커 데이터 갱신 작업이 완료되었습니다.");
    }
}
