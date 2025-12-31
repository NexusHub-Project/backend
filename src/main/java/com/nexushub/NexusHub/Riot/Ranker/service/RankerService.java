package com.nexushub.NexusHub.Riot.Ranker.service;

import com.nexushub.NexusHub.Common.Exception.Fail.TooManyRequestFail;
import com.nexushub.NexusHub.Common.Exception.RiotAPI.CannotFoundSummoner;
import com.nexushub.NexusHub.Riot.Ranker.domain.Ranker;
import com.nexushub.NexusHub.Riot.Ranker.domain.Tier;
import com.nexushub.NexusHub.Riot.Ranker.dto.FromRiotRankerResDto;
import com.nexushub.NexusHub.Riot.Ranker.dto.RiotRankerDto;
import com.nexushub.NexusHub.Riot.Ranker.repository.RankerRepository;
import com.nexushub.NexusHub.Riot.RiotInform.dto.RiotAccountDto;
import com.nexushub.NexusHub.Riot.RiotInform.service.RiotApiService;
import com.nexushub.NexusHub.Riot.Summoner.domain.Summoner;
import com.nexushub.NexusHub.Riot.Summoner.service.SummonerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RankerService {
    private final SummonerService summonerService;
    private final RiotApiService riotApiService;
    private final RankerRepository rankerRepository;

    public void saveChallenger() throws InterruptedException {
        // 챌린저 티어 유저들이 RiotRankerDto 담겨져 있음 puuid로 구분 해야 함
        List<RiotRankerDto> challengers = riotApiService.getLeagueByTier(Tier.CHALLENGER).getEntries();
        Integer ranking = 1;
        challengers.sort((a, b) -> Integer.compare(b.getLeaguePoints(), a.getLeaguePoints()));

        // 한번에 저장하는게 나음
        List<Ranker> rankerList = new ArrayList<>();
        int i = 1;
        for (RiotRankerDto challengerUserDto : challengers) { // 순회하기
            log.info("rank : {} puuid : {}", i++, challengerUserDto.getPuuid());

            Optional<Summoner> optionalSummoner = summonerService.getSummonerByPuuid(challengerUserDto.getPuuid()); // puuid로 우리 DB에 Summoner 저장되어 있는지 체크
            Summoner summoner = null;
            if (optionalSummoner.isEmpty()){ // 저장되어 있지 않는 사람이야
                RiotAccountDto newInform = getNewSummonerInformation(challengerUserDto.getPuuid());
                if (newInform == null){
                    continue;
                }
                summoner = summonerService.saveSummoner(newInform);
            }
            else {
                summoner = optionalSummoner.get();
            }

            // Summoner 객체 있든 없든 모두 가져 왔음
            Ranker ranker = Ranker.of(summoner, Tier.CHALLENGER, challengerUserDto.getLeaguePoints(), ranking++);
            rankerList.add(ranker);

        }
        rankerRepository.saveAll(rankerList);
    }
    public void saveGrandMasters() throws InterruptedException {
        // 그랜드 마스터 티어 유저들이 RiotRankerDto 담겨져 있음 puuid로 구분 해야 함
        List<RiotRankerDto> grandMasters = riotApiService.getLeagueByTier(Tier.GRANDMASTER).getEntries();
        Integer ranking = 301;
        grandMasters.sort((a, b) -> Integer.compare(b.getLeaguePoints(), a.getLeaguePoints()));

        // 한번에 저장하는게 나음
        List<Ranker> rankerList = new ArrayList<>();
        int i = 301;
        for (RiotRankerDto grandMasterUserDto : grandMasters) { // 순회하기
            log.info("rank : {} puuid : {}", i++, grandMasterUserDto.getPuuid());

            Optional<Summoner> optionalSummoner = summonerService.getSummonerByPuuid(grandMasterUserDto.getPuuid()); // puuid로 우리 DB에 Summoner 저장되어 있는지 체크
            Summoner summoner = null;
            if (optionalSummoner.isEmpty()){ // 저장되어 있지 않는 사람이야
                RiotAccountDto newInform = getNewSummonerInformation(grandMasterUserDto.getPuuid());
                if (newInform == null){
                    continue;
                }
                summoner = summonerService.saveSummoner(newInform);
            }
            else {
                summoner = optionalSummoner.get();
            }

            // Summoner 객체 있든 없든 모두 가져 왔음
            Ranker ranker = Ranker.of(summoner, Tier.CHALLENGER, grandMasterUserDto.getLeaguePoints(), ranking++);
            rankerList.add(ranker);

        }
        rankerRepository.saveAll(rankerList);
    }
    public void saveMasters() throws InterruptedException {
        // 그랜드 마스터 티어 유저들이 RiotRankerDto 담겨져 있음 puuid로 구분 해야 함
        List<RiotRankerDto> masters = riotApiService.getLeagueByTier(Tier.MASTER).getEntries();
        Integer ranking = 1001;
        masters.sort((a, b) -> Integer.compare(b.getLeaguePoints(), a.getLeaguePoints()));

        // 한번에 저장하는게 나음
        List<Ranker> rankerList = new ArrayList<>();
        int i = 1001;
        for (RiotRankerDto masterUserDto : masters) { // 순회하기
            log.info("rank : {} puuid : {}", i++, masterUserDto.getPuuid());

            Optional<Summoner> optionalSummoner = summonerService.getSummonerByPuuid(masterUserDto.getPuuid()); // puuid로 우리 DB에 Summoner 저장되어 있는지 체크
            Summoner summoner = null;
            if (optionalSummoner.isEmpty()){ // 저장되어 있지 않는 사람이야
                RiotAccountDto newInform = getNewSummonerInformation(masterUserDto.getPuuid());
                if (newInform == null){
                    continue;
                }
                summoner = summonerService.saveSummoner(newInform);
            }
            else {
                summoner = optionalSummoner.get();
            }

            // Summoner 객체 있든 없든 모두 가져 왔음
            Ranker ranker = Ranker.of(summoner, Tier.CHALLENGER, masterUserDto.getLeaguePoints(), ranking++);
            rankerList.add(ranker);

        }
        rankerRepository.saveAll(rankerList);
    }
    private RiotAccountDto getNewSummonerInformation(String puuid) throws InterruptedException {

        for (int i=0; i<3; i++){
            try{
                RiotAccountDto summonerByPuuid = riotApiService.getSummonerByPuuid(puuid);
                if (summonerByPuuid != null) {
                    return summonerByPuuid;
                }
            } catch (TooManyRequestFail e){
                if (i < 2) {
                    log.warn("API 요청 제한(429). {}초 후 재시도... (시도 {}/3)", 125, i + 1);
                    Thread.sleep(125000);
                }
            } catch (CannotFoundSummoner e) {
                log.info("소환사 정보 없음 (404). 재시도 안 함.");
                return null;
            }
        }
        log.error("3회 재시도 실패. PUUID: {}", puuid);
        return null;
    }
}