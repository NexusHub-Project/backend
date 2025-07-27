package com.nexushub.NexusHub.Summoner.controller.v1;

import com.nexushub.NexusHub.Exception.RiotAPI.CannotFoundSummoner;
import com.nexushub.NexusHub.Match.dto.MatchDto;
import com.nexushub.NexusHub.Match.dto.v2.MatchDataDto;
import com.nexushub.NexusHub.Match.service.MatchService;
import com.nexushub.NexusHub.Riot.dto.MasteryDto;
import com.nexushub.NexusHub.Statistics.dto.ChampionSeasonStatisticsDto;
import com.nexushub.NexusHub.Summoner.domain.Summoner;
import com.nexushub.NexusHub.Summoner.dto.SummonerDto;
import com.nexushub.NexusHub.Summoner.dto.SummonerRequestDto;
import com.nexushub.NexusHub.Summoner.dto.SummonerResponseDto;
import com.nexushub.NexusHub.Summoner.service.SummonerService;
import com.nexushub.NexusHub.User.service.UserService;
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
    private final UserService userService;

    // 티어 정보 검색
    @PostMapping("/tier")
    public ResponseEntity<SummonerResponseDto> summonerTierInfo(@RequestBody SummonerDto.Request dto) throws CannotFoundSummoner {
        Summoner summonerTierInfo = summonerService.getSummonerTierInfoV2(dto);
        SummonerResponseDto resDto = new SummonerResponseDto(summonerTierInfo);

        return ResponseEntity.ok(resDto);

    }

    // 숙련도 정보 검색 (모든 챔피언의 숙련도 리턴)
    @GetMapping("/mastery")
    public ResponseEntity<List<MasteryDto>> summonerMasteryInfo(@RequestBody SummonerRequestDto dto) throws CannotFoundSummoner {
        List<MasteryDto> masteryInfo = summonerService.getSummonerMasteryInfo(dto);
        return ResponseEntity.ok(masteryInfo);
    }


    // 전적 검색
    @GetMapping("/matches/v1")
    public ResponseEntity<String[]> summonerMatchesV1(@RequestBody SummonerRequestDto dto) throws CannotFoundSummoner{
        String[] matches = summonerService.getSummonerMatchesId(dto);
        return ResponseEntity.ok(matches);
    }
    @GetMapping("/matches/v2")
    public ResponseEntity<List<MatchDto>> summonerMatchesV2(@RequestBody SummonerRequestDto dto) throws CannotFoundSummoner{
        List<MatchDto> dtos = summonerService.getSummonerMatchesInfo(dto);
        return ResponseEntity.ok(dtos);
    }
    @GetMapping("/matches/v3")
    public ResponseEntity<List<MatchDataDto>> summonerMatchesV3(@RequestBody SummonerRequestDto dto) throws CannotFoundSummoner {

        log.info("step 1) Summoner Controller : summonerMatchesV3 / dto : {}", dto.toString());
        List<MatchDataDto> matchDtos = summonerService.getSummonerMatchesInfoV1(dto);

        return ResponseEntity.ok(matchDtos);
    }


    @GetMapping("/matchInfo")
    public ResponseEntity<MatchDto> summonerMatchInfo(@RequestParam String matchId) {
        log.info("matchId: " + matchId);
        MatchDto matchInfo = matchService.getMatchInfoById(matchId);
        return ResponseEntity.ok(matchInfo);
    }

    // most 챔피언 검색
    @GetMapping("/most")
    public ResponseEntity<List<ChampionSeasonStatisticsDto>> summonerMostInfo(@RequestBody SummonerRequestDto dto) throws CannotFoundSummoner{

            /*    모스트 챔피언 V1 -> 차이는 새롭게 DTO를 정의해서 필요한 것만 뽑아 왔다
            Map<Long, ChampionStatsDto> stats = matchService.getSeasonChampionStatsV1(dto);

            // Map의 값(ChampionStatsDto)들을 리스트로 변환한 뒤, '플레이 횟수'가 많은 순서로 정렬
            List<ChampionStatsDto> sortedStats = stats.values().stream()
                    .sorted(Comparator.comparingInt(ChampionStatsDto::getGamesPlayed).reversed())
                    .collect(Collectors.toList());
            */
        List<ChampionSeasonStatisticsDto> sortedStats = matchService.getSeasonChampionStatsV2(dto);
        return ResponseEntity.ok(sortedStats);
    }
}
