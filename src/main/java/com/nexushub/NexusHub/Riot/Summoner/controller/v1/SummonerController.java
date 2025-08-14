package com.nexushub.NexusHub.Riot.Summoner.controller.v1;

import com.nexushub.NexusHub.Common.Exception.RiotAPI.CannotFoundSummoner;
import com.nexushub.NexusHub.Common.Filter.GameNameTrimFilter;
import com.nexushub.NexusHub.Riot.Match.dto.InfoDto;
import com.nexushub.NexusHub.Riot.Match.dto.MatchDto;
import com.nexushub.NexusHub.Riot.Match.dto.v2.MatchDataDto;
import com.nexushub.NexusHub.Riot.Match.dto.v3.MatchInfoResDto;
import com.nexushub.NexusHub.Riot.Match.service.MatchService;
import com.nexushub.NexusHub.Riot.RiotInform.dto.MasteryDto;
import com.nexushub.NexusHub.Riot.RiotInform.dto.Ranker.ChallengerLeagueDto;
import com.nexushub.NexusHub.Riot.RiotInform.dto.Ranker.ChallengersResDto;
import com.nexushub.NexusHub.Riot.RiotInform.service.RiotApiService;
import com.nexushub.NexusHub.Riot.Summoner.dto.SummonerTierResDto;
import com.nexushub.NexusHub.Web.Statistics.dto.ChampionSeasonStatisticsDto;
import com.nexushub.NexusHub.Riot.Summoner.service.SummonerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Queue;

@RestController
@RequestMapping("/api/v1/summoner")
@Slf4j
@RequiredArgsConstructor
public class SummonerController {
    private final SummonerService summonerService;
    private final MatchService matchService;
    private final RiotApiService riotApiService;

    /** gameName과 tagLine을 통해서 티어 정보를 검색하는 API이다.
     * - DTO 수정 완료
     * @param gameName
     * @param tagLine
     * @return Summoner
     * @throws CannotFoundSummoner
     */
    @GetMapping("/tier")
    public ResponseEntity<SummonerTierResDto> summonerTierInfo(@RequestParam String gameName, @RequestParam String tagLine) throws CannotFoundSummoner {
        log.info("SummonerController - summonerTierInfo /tier 호출 ");
        SummonerTierResDto summonerTierInfo = summonerService.getSummonerTierInfo(gameName, tagLine);
        return ResponseEntity.ok(summonerTierInfo);
    }

    /** gameName과 tagLine을 통해서 숙련도 정보를 검색하는 API이다. -> 리신 숙련도 15만점 등
     *
     * @param gameName
     * @param tagLine
     * @return
     * @throws CannotFoundSummoner
     */
    @GetMapping("/mastery")
    public ResponseEntity<List<MasteryDto>> summonerMasteryInfo(@RequestParam String gameName, @RequestParam String tagLine) throws CannotFoundSummoner {
        log.info("SummonerController - summonerMasteryInfo /mastery 호출 ");
        List<MasteryDto> masteryInfo = summonerService.getSummonerMasteryInfo(gameName, tagLine);
        return ResponseEntity.ok(masteryInfo);
    }


    /** gameName과 tagLine을 통해서 전반적인 최근 20게임의 전적을 검색된 사람 기준으로 보내주는 API이다.
     *
     * @param gameName
     * @param tagLine
     * @return
     * @throws CannotFoundSummoner
     */
    @GetMapping("/matches")
    public ResponseEntity<Queue<MatchInfoResDto>> summonerMatches(@RequestParam String gameName, @RequestParam String tagLine) throws CannotFoundSummoner {
        log.info("SummonerController - summonerMatches /matches 호출 ");
        Queue<MatchInfoResDto> matchDtos = summonerService.getSummonerMatches(gameName, tagLine);
        return ResponseEntity.ok(matchDtos);
    }

    /** matchId를 받아서 해당 게임의 상세한 정보를 보내주는 API이다.
     *
     * @param matchId 게임 정보의 식별 id 값
     * @return
     */
    @GetMapping("/matchInfo")
    public ResponseEntity<MatchDto> summonerMatchInfo(@RequestParam String matchId) {
        log.info("SummonerController - summonerMatchInfo /matchInfo 호출 ");
        MatchDto matchInfo = matchService.getMatchInfoById(matchId);
        InfoDto info = matchInfo.getInfo();
        log.info("info : {}", info);
        return ResponseEntity.ok(matchInfo);
    }

    /** 🚨미완성 🚨 전적을 검색한 소환사의 이번 시즌에 가장 많이 챔피언을 보내주는 API이다.
     *
     * @param gameName
     * @param tagLine
     * @return
     * @throws CannotFoundSummoner
     */
    @GetMapping("/most")
    public ResponseEntity<List<ChampionSeasonStatisticsDto>> summonerMostInfo(@RequestParam String gameName, @RequestParam String tagLine) throws CannotFoundSummoner{
        log.info("SummonerController - summonerMostInfo /most 호출 ");
        String puuid = summonerService.findPuuid(gameName, tagLine, summonerService.findSummoner(gameName, tagLine));
        List<ChampionSeasonStatisticsDto> sortedStats = matchService.getStatisticsOfMostChampion(puuid);
        return ResponseEntity.ok(sortedStats);
    }

    /** 솔로랭킹을 보여주는 API이다.
     *
     * @return
     */
    @GetMapping("/search/challenger")
    public ResponseEntity<List<ChallengersResDto>> searchChallengers(){
        log.info("SummonerController - searchChallengers /search/challenger 호출 ");
        ChallengerLeagueDto challengers = riotApiService.getChallengers();
        List<ChallengersResDto> dtos = summonerService.setChallengersData(challengers);
        return ResponseEntity.ok(dtos);
    }
}
