package com.nexushub.NexusHub.Riot.Ranker.controller;

import com.nexushub.NexusHub.Common.Exception.RiotAPI.CannotFoundSummoner;
import com.nexushub.NexusHub.Riot.Ranker.Sheduler.RankerScheduler;
import com.nexushub.NexusHub.Riot.Ranker.domain.Ranker;
import com.nexushub.NexusHub.Riot.Ranker.domain.Tier;
import com.nexushub.NexusHub.Riot.Ranker.dto.FromRiotRankerResDto;
import com.nexushub.NexusHub.Riot.Ranker.dto.RankerResDto;
import com.nexushub.NexusHub.Riot.Ranker.dto.RiotRankerDto;
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

import javax.swing.*;
import java.util.ArrayList;
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
    private final RankerScheduler rankerScheduler;

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
    @GetMapping("/store-rankers/challenger")
    public ResponseEntity<String> storeRankers() throws InterruptedException {
        // 단순 디비에 저장하는 용도
        rankerService.saveChallenger();

        return ResponseEntity.ok("저장 완료");
    }
    @GetMapping("/store-rankers/grandmaster")
    public ResponseEntity<String> storeRankersG() throws InterruptedException {
        // 단순 디비에 저장하는 용도
        rankerService.saveGrandMasters();

        return ResponseEntity.ok("저장 완료");
    }
    @GetMapping("/store-rankers/master")
    public ResponseEntity<String> storeRankersM() throws InterruptedException {
        // 단순 디비에 저장하는 용도
        rankerService.saveMasters();

        return ResponseEntity.ok("저장 완료");
    }

    @GetMapping("/v2/ranker/challenger/{page}")
    public ResponseEntity<Queue<RankerResDto>> getChallRanks(@PathVariable int page){
        return ResponseEntity.ok(rankerService.getRankersByKey("ranking:challenger",page));
    }
    @GetMapping("/v2/ranker/grandmaster/{page}")
    public ResponseEntity<Queue<RankerResDto>> getGrandMasterRanks(@PathVariable int page){
        return ResponseEntity.ok(rankerService.getRankersByKey("ranking:grandmaster",page));
    }
    @GetMapping("/v2/ranker/master/{page}")
    public ResponseEntity<Queue<RankerResDto>> getMASTERRanks(@PathVariable int page){
        return ResponseEntity.ok(rankerService.getRankersByKey("ranking:master",page));
    }

    @GetMapping("/v2/ranker/all/{page}")
    public ResponseEntity<Queue<RankerResDto>> getALLRanks(@PathVariable int page){
        return ResponseEntity.ok(rankerService.getRankersByKey("ranking:all",page));
    }
    @GetMapping("/v2/ranker/test/ranking")
    public String test(){
        rankerScheduler.scheduleRankingUpdate();
        return "Good";
    }
    @GetMapping("/v2/ranker/puuid/gm")
    public ResponseEntity<List<String>> getPuuidRanker() throws CannotFoundSummoner {
        FromRiotRankerResDto challengersV2 = riotApiService.getChallengersV2(Tier.CHALLENGER);
        List<String> list = new ArrayList<>();
        for (RiotRankerDto entry : challengersV2.getEntries()) {
            list.add(entry.getPuuid());
        }
        return ResponseEntity.ok(list);
    }
    @GetMapping("/v2/ranker/puuid/c")
    public ResponseEntity<List<String>> getPuuidRancccker() throws CannotFoundSummoner {
        FromRiotRankerResDto challengersV2 = riotApiService.getChallengersV2(Tier.GRANDMASTER);
        List<String> list = new ArrayList<>();
        for (RiotRankerDto entry : challengersV2.getEntries()) {
            list.add(entry.getPuuid());
        }
        return ResponseEntity.ok(list);
    }

}
