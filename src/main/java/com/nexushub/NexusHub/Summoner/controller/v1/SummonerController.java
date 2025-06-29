package com.nexushub.NexusHub.Summoner.controller.v1;

import com.nexushub.NexusHub.Exception.RiotAPI.CannotFoundSummoner;
import com.nexushub.NexusHub.Match.dto.MatchDto;
import com.nexushub.NexusHub.Match.service.MatchService;
import com.nexushub.NexusHub.Riot.dto.MasteryDto;
import com.nexushub.NexusHub.Statistics.dto.ChampionSeasonStatisticsDto;
import com.nexushub.NexusHub.Summoner.domain.Summoner;
import com.nexushub.NexusHub.Summoner.dto.SummonerRequestDto;
import com.nexushub.NexusHub.Summoner.service.SummonerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/summoner")
@Slf4j
@RequiredArgsConstructor
public class SummonerController {
    private final SummonerService summonerService;
    private final MatchService matchService;

    // 티어 정보 검색
    @PostMapping("/tier")
    public ResponseEntity<?> summonerTierInfo(@RequestBody SummonerRequestDto dto) {
        try {
            log.info("Summoner request12321321312312312: {}", dto);
            Summoner summonerTierInfo = summonerService.getSummonerTierInfoV2(dto);
            return ResponseEntity.ok(summonerTierInfo);
        } catch (CannotFoundSummoner e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    // 숙련도 정보 검색 (모든 챔피언의 숙련도 리턴)
    @GetMapping("/mastery")
    public ResponseEntity<?> summonerMasteryInfo(@RequestBody SummonerRequestDto dto) {
        try{
            List<MasteryDto> masteryInfo = summonerService.getSummonerMasteryInfo(dto);
            return ResponseEntity.ok(masteryInfo);
        } catch (CannotFoundSummoner e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    // 전적 검색
    @GetMapping("/matches/v1")
    public ResponseEntity<?> summonerMatchesV1(@RequestBody SummonerRequestDto dto) {
        try{
            String[] matches = summonerService.getSummonerMatchesId(dto);
            return ResponseEntity.ok(matches);
        } catch (CannotFoundSummoner e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/matches/v2")
    public ResponseEntity<?> summonerMatchesV2(@RequestBody SummonerRequestDto dto) {
        try{
            List<MatchDto> dtos = summonerService.getSummonerMatchesInfo(dto);
            return ResponseEntity.ok(dtos);

        } catch (CannotFoundSummoner e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/matchInfo")
    public ResponseEntity<?> summonerMatchInfo(@RequestParam String matchId) {
        log.info("matchId: " + matchId);
        MatchDto matchInfo = matchService.getMatchInfoById(matchId);
        return ResponseEntity.ok(matchInfo);

    }

    // most 챔피언 검색
    @GetMapping("/most")
    public ResponseEntity<?> summonerMostInfo(@RequestBody SummonerRequestDto dto) {
        try {
            /*    모스트 챔피언 V1 -> 차이는 새롭게 DTO를 정의해서 필요한 것만 뽑아 왔다
            Map<Long, ChampionStatsDto> stats = matchService.getSeasonChampionStatsV1(dto);

            // Map의 값(ChampionStatsDto)들을 리스트로 변환한 뒤, '플레이 횟수'가 많은 순서로 정렬
            List<ChampionStatsDto> sortedStats = stats.values().stream()
                    .sorted(Comparator.comparingInt(ChampionStatsDto::getGamesPlayed).reversed())
                    .collect(Collectors.toList());
            */
            List<ChampionSeasonStatisticsDto> sortedStats = matchService.getSeasonChampionStatsV2(dto);

            return ResponseEntity.ok(sortedStats);
        } catch (CannotFoundSummoner e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
