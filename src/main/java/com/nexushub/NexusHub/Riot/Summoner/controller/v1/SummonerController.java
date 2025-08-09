package com.nexushub.NexusHub.Riot.Summoner.controller.v1;

import com.nexushub.NexusHub.Common.Exception.RiotAPI.CannotFoundSummoner;
import com.nexushub.NexusHub.Riot.Match.dto.MatchDto;
import com.nexushub.NexusHub.Riot.Match.dto.v2.MatchDataDto;
import com.nexushub.NexusHub.Riot.Match.service.MatchService;
import com.nexushub.NexusHub.Riot.RiotInform.dto.MasteryDto;
import com.nexushub.NexusHub.Riot.RiotInform.dto.Ranker.ChallengerLeagueDto;
import com.nexushub.NexusHub.Riot.RiotInform.dto.Ranker.ChallengersResponseDto;
import com.nexushub.NexusHub.Riot.RiotInform.dto.RiotAccountDto;
import com.nexushub.NexusHub.Riot.RiotInform.service.RiotApiService;
import com.nexushub.NexusHub.Web.Statistics.dto.ChampionSeasonStatisticsDto;
import com.nexushub.NexusHub.Riot.Summoner.domain.Summoner;
import com.nexushub.NexusHub.Riot.Summoner.dto.SummonerDto;
import com.nexushub.NexusHub.Riot.Summoner.dto.SummonerRequestDto;
import com.nexushub.NexusHub.Riot.Summoner.service.SummonerService;
import com.nexushub.NexusHub.Web.User.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final RiotApiService riotApiService;

    // 티어 정보 검색
    @GetMapping("/tier")
    public ResponseEntity<Summoner> summonerTierInfo(@RequestParam String gameName, @RequestParam String tagLine) throws CannotFoundSummoner {
        Summoner summonerTierInfo = summonerService.getSummonerTierInfoV2(gameName, tagLine);
        return ResponseEntity.ok(summonerTierInfo);
    }

    // 숙련도 정보 검색 (모든 챔피언의 숙련도 리턴)
    @GetMapping("/mastery")
    public ResponseEntity<List<MasteryDto>> summonerMasteryInfo(@RequestParam String gameName, @RequestParam String tagLine) throws CannotFoundSummoner {
        List<MasteryDto> masteryInfo = summonerService.getSummonerMasteryInfo(gameName, tagLine);
        return ResponseEntity.ok(masteryInfo);
    }


    // 전적 검색
    @GetMapping("/matches")
    public ResponseEntity<List<MatchDataDto>> summonerMatchesV3(@RequestParam String gameName, @RequestParam String tagLine) throws CannotFoundSummoner {

        SummonerRequestDto dto = new SummonerRequestDto();
        dto.setGameName(gameName);
        dto.setTagLine(tagLine);
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
    public ResponseEntity<List<ChampionSeasonStatisticsDto>> summonerMostInfo(@RequestParam String gameName, @RequestParam String tagLine) throws CannotFoundSummoner{
        // 필요한 거 : gameName, tagLine


            /*    모스트 챔피언 V1 -> 차이는 새롭게 DTO를 정의해서 필요한 것만 뽑아 왔다
            Map<Long, ChampionStatsDto> stats = matchService.getSeasonChampionStatsV1(dto);

            // Map의 값(ChampionStatsDto)들을 리스트로 변환한 뒤, '플레이 횟수'가 많은 순서로 정렬
            List<ChampionStatsDto> sortedStats = stats.values().stream()
                    .sorted(Comparator.comparingInt(ChampionStatsDto::getGamesPlayed).reversed())
                    .collect(Collectors.toList());
            */
        List<ChampionSeasonStatisticsDto> sortedStats = matchService.getSeasonChampionStatsV2(gameName, tagLine);
        return ResponseEntity.ok(sortedStats);
    }
    @GetMapping("/search/challenger")
    public ResponseEntity<List<ChallengersResponseDto>> searchChallengers(){
        ChallengerLeagueDto challengers = riotApiService.getChallengers();
        List<ChallengersResponseDto> dtos = summonerService.setChallengersData(challengers);
        return ResponseEntity.ok(dtos);
    }
}
