package com.nexushub.NexusHub.Riot.Match.service;

import com.nexushub.NexusHub.Common.Exception.RiotAPI.CannotFoundSummoner;
import com.nexushub.NexusHub.Riot.Data.Champion.ChampionRepository;
import com.nexushub.NexusHub.Riot.Match.domain.Match;
import com.nexushub.NexusHub.Riot.Match.dto.ChampionStatsDto;
import com.nexushub.NexusHub.Riot.Match.dto.DataDto;
import com.nexushub.NexusHub.Riot.Match.dto.MatchDto;
import com.nexushub.NexusHub.Riot.Match.dto.ParticipantDto;
import com.nexushub.NexusHub.Riot.Match.dto.minimal.MinimalMatchDto;
import com.nexushub.NexusHub.Riot.Match.repository.MatchParticipantRepository;
import com.nexushub.NexusHub.Riot.Match.repository.MatchRepository;
import com.nexushub.NexusHub.Riot.RiotInform.service.RiotApiService;
import com.nexushub.NexusHub.Web.Statistics.dto.ChampionSeasonStatisticsDto;
import com.nexushub.NexusHub.Riot.Summoner.dto.SummonerRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchService {
    private final RiotApiService riotApiService;
    private final ChampionRepository championRepository;
    private final MatchRepository matchRepository;
    private final MatchParticipantRepository matchParticipantRepository;


    @Value("${riot.season2025-1}")
    private long seasonStartTime;

    public MatchDto getMatchInfo(String matchId, String puuid) {
        // 1) 매치 정보 받아오기
        MatchDto matchInfo = riotApiService.getMatchInfo(matchId);

        // 2) 새로운 값 세팅하기 - ourScore(분석 점수), teamScore(팀운 점수)
        // 2-1) participants List로 받아오기
        // 2-2) 점수 계산해서 넣어주기 (지금은 랜덤)
        List<ParticipantDto> participants = matchInfo.getInfo().getParticipants();
        calculateOurScore(participants);
        calculateTeamScore(participants);

        // 3) 전적을 검색한 당사자의 데이터 세팅하기
        // 3-1) 어떤 Participant가 당사자인지 찾기
        for (ParticipantDto participant : participants) {
            if (participant.getPuuid().equals(puuid)) {
                matchInfo.setMyData(DataDto.setDataDto(participant));
                break;
            }
        }
        return matchInfo;
    }
    public Map<Long, ChampionStatsDto> getSeasonChampionStatsV1(SummonerRequestDto dto) throws CannotFoundSummoner {
        String puuid = riotApiService.getSummonerPuuid(dto.getGameName(), dto.getTagLine());


        // 1) 이번 시즌의 전적 matchID를 가져옴
        List<String> matchIds = riotApiService.getMatchIdByPuuid(puuid, seasonStartTime);
        log.info("{}의 시즌 전적 {}개 분석 시작", puuid, matchIds.size());

        // 2) 챔피언 별 통계를 저장할 MAP 생성
        Map<Long, ChampionStatsDto> statsMap = new HashMap<>();

        // 3) 각 전적 순회하며 데이터 집계
        for (String matchId : matchIds) {
            // 3-1) 전적의 matchID로 경기의 상세 정보를 가져 옴
            MatchDto matchInfo = riotApiService.getMatchInfo(matchId);

            // API 호출 제한을 피하기 위해 각 호출 사이에 약간의 딜레이
            try { Thread.sleep(50); } catch (InterruptedException e) {}

            if (matchInfo == null) continue;

            // 4) 매치 정보에서 내 전적 뽑기 ; MatchDto에는 List<Participant>가 있음
            matchInfo.getInfo().getParticipants().stream()
                    .filter(p -> puuid.equals(p.getPuuid()))
                    .findFirst() // 첫번째로 찾은 사람이 내가 플레이한 것
                    .ifPresent(myPerformance ->{

                        // 내가 플레이한 챔피언의 id (리신 - 64)
                        long championId = myPerformance.getChampionId();

                        // 5) Map에서 해당 챔피언의 통계 객체를 가져오거나 새로 만듦 ; Map에 championId로 ChampionStatsDto가 생성이 되어 있으면 그걸 반환, 아니면 생성 후 반환
                        ChampionStatsDto stats = statsMap.computeIfAbsent(championId, id -> new ChampionStatsDto(id));

                        // +) 챔피언 이름 추가
                        if (stats.getChampionName() == null) {
                            championRepository.findById(championId).ifPresent(champion -> {
                                stats.setChampionName(champion.getNameKo()); // 한국어 이름 설정
                            });
                        }
                        // 6) 통계 누적 하기
                        stats.incrementGamesPlayed(); // 플레이 횟수 ++
                        if (myPerformance.getWin()){  // 이겼으면 횟수 ++
                            stats.incrementWins();
                        }
                        stats.addKda(myPerformance.getKills(), myPerformance.getDeaths(), myPerformance.getAssists());
                    });
        }
        return statsMap;
    }

    public List<ChampionSeasonStatisticsDto> getSeasonChampionStatsV2(String gameName, String tagLine) throws CannotFoundSummoner {
        String puuid = riotApiService.getSummonerPuuid(gameName, tagLine);


        // 1) 이번 시즌의 전적 matchID를 가져옴
        List<String> matchIds = riotApiService.getMatchIdByPuuid(puuid, seasonStartTime);
        log.info("{}의 시즌 전적 {}개 분석 시작", puuid, matchIds.size());

        // 2) 챔피언 별 통계를 저장할 MAP 생성
        Map<Long, ChampionSeasonStatisticsDto> statsMap = new HashMap<>();

        // 3) 각 전적 순회하며 데이터 집계
        for (String matchId : matchIds) {
            // 3-1) 전적의 matchID로 경기의 상세 정보를 가져 옴
//            MatchDto matchInfo = riotApiService.getMatchInfo(matchId);

            // API 호출 제한을 피하기 위해 각 호출 사이에 약간의 딜레이
            try { Thread.sleep(500); } catch (InterruptedException e) {}

            MinimalMatchDto minimalMatchInfo = riotApiService.getMinimalMatchInfo(matchId);
            log.info("minimalMatchInfo : {}", minimalMatchInfo);
            if (minimalMatchInfo == null || minimalMatchInfo.getInfo() == null) {
                log.warn("매치 정보를 가져오지 못했습니다: {}", matchId);
                continue;
            }

            long gameDuration = minimalMatchInfo.getInfo().getGameDuration();


            // 4) 해당 게임에서 내가 플레이한 정보를 찾아서 통계에 누적 함

            minimalMatchInfo.getInfo().getParticipants().stream()
                    .filter(p -> puuid.equals(p.getPuuid()))
                    .findFirst()
                    .ifPresent(myPerformance ->{
                        // 5) Map에서 분석 중인 게임에서 플레이한 챔피언이 없는 경우 가져옴, 아니면 그걸 반환
                        long championId = myPerformance.getChampionId();

                        ChampionSeasonStatisticsDto stats = statsMap.computeIfAbsent(championId, id -> new ChampionSeasonStatisticsDto(championId));

                        // 6) 챔피언 이름 주입
                        if (stats.getChampionName() == null) {
                            championRepository.findById(championId).ifPresent(champion -> {
                                stats.setChampionName(champion.getNameKo());
                            });
                        }

                        // 7) 경기 결과를 통계 객체에 넣어서 누적 시킴
                        stats.addMatchResult(
                                myPerformance.isWin(), myPerformance.getKills(), myPerformance.getDeaths(), myPerformance.getAssists(),
                                myPerformance.getTotalDamageDealtToChampions(), gameDuration
                        );
                    });


        }
        // 8) 리스트로 변환해서 반환하기
        return statsMap.values().stream()
                .sorted(Comparator.comparingInt(ChampionSeasonStatisticsDto::getGamesPlayed).reversed())
                .collect(Collectors.toList());
    }
    public MatchDto getMatchInfoById(String matchId) {
        return riotApiService.getMatchInfo(matchId);
    }

    public Optional<Match> getMatchByMatchId(String matchId) {
        return matchRepository.findMatchByMatchId(matchId);
    }

    public void save(Match newMatch){
        matchRepository.save(newMatch);
    }

    private void calculateOurScore(List<ParticipantDto> participants) {
        participants.forEach(participant -> participant.setOurScore(
                        (int) (Math.random()*80)+20
        ));
    }
    private void calculateTeamScore(List<ParticipantDto> participants) {
        participants.forEach(participant -> participant.setTeamScore(
                (int) (Math.random()*80)+20
        ));
    }
}
