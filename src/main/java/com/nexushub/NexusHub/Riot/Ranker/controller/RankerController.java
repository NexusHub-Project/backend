package com.nexushub.NexusHub.Riot.Ranker.controller;

import com.nexushub.NexusHub.Common.Exception.RiotAPI.CannotFoundSummoner;
import com.nexushub.NexusHub.Riot.Ranker.Sheduler.RankerScheduler;
import com.nexushub.NexusHub.Riot.Ranker.domain.Ranker;
import com.nexushub.NexusHub.Riot.Ranker.domain.Tier;
import com.nexushub.NexusHub.Riot.Ranker.dto.FromRiotRankerResDto;
import com.nexushub.NexusHub.Riot.Ranker.dto.RankerFinalResDto;
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
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Queue;

@RestController
@RequestMapping("/api/v1/ranker")
@Slf4j
@RequiredArgsConstructor
public class RankerController {

    private final RankerService rankerService;
    private final RankerScheduler rankerScheduler;

    @GetMapping("/challenger/{page}")
    public ResponseEntity<RankerFinalResDto> getChallRanks(@PathVariable int page){
        Queue<RankerResDto> rankersByKey = rankerService.getRankersByKey("ranking:challenger", page);
        return ResponseEntity.ok(RankerFinalResDto.from(rankersByKey, 3L, (long) page));
    }
    @GetMapping("/grandmaster/{page}")
    public ResponseEntity<RankerFinalResDto> getGrandMasterRanks(@PathVariable int page){
        Queue<RankerResDto> rankersByKey = rankerService.getRankersByKey("ranking:grandmaster", page);
        return ResponseEntity.ok(RankerFinalResDto.from(rankersByKey, 7L, (long) page));
    }
    @GetMapping("/master/{page}")
    public ResponseEntity<RankerFinalResDto> getMASTERRanks(@PathVariable int page){

        Queue<RankerResDto> rankersByKey = rankerService.getRankersByKey("ranking:master", page);
        Long masterPageSize = rankerService.getMasterPageSize();

        return ResponseEntity.ok(RankerFinalResDto.from(rankersByKey, masterPageSize, (long) page));
    }

    @GetMapping("/all/{page}")
    public ResponseEntity<RankerFinalResDto> getALLRanks(@PathVariable int page){
        Queue<RankerResDto> rankersByKey = rankerService.getRankersByKey("ranking:all", page);
        Long allPageSize = rankerService.getAllPageSize();
        return ResponseEntity.ok(RankerFinalResDto.from(rankersByKey, allPageSize, (long) page));
    }

}
