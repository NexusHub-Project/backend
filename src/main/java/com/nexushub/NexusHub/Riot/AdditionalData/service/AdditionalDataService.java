package com.nexushub.NexusHub.Riot.AdditionalData.service;

import com.nexushub.NexusHub.Common.Exception.Fail.TooManyRequestFail;
import com.nexushub.NexusHub.Common.Exception.RiotAPI.CannotFoundSummoner;
import com.nexushub.NexusHub.Riot.Ranker.service.RankerService;
import com.nexushub.NexusHub.Riot.RiotInform.dto.ProfileResDto;
import com.nexushub.NexusHub.Riot.RiotInform.dto.RiotAccountDto;
import com.nexushub.NexusHub.Riot.RiotInform.service.RiotApiService;
import com.nexushub.NexusHub.Riot.Summoner.domain.Summoner;
import com.nexushub.NexusHub.Riot.Summoner.service.SummonerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdditionalDataService {

    private final RiotApiService riotApiService;
    private final RankerService rankerService;
    private final SummonerService summonerService;

    public void downloadRankersProfile() throws InterruptedException {
        List<String> challengerPuuid =
                rankerService.getRankersPuuid("ranking:challenger");
        List<String> gmPuuid =
                rankerService.getRankersPuuid("ranking:grandmaster");
        List<String> masterPuuid =
                rankerService.getRankersPuuid("ranking:master");
        storeProfile(challengerPuuid);
        log.info("<< CHALLENGER ICON & LEVEL DONE >>");
        storeProfile(gmPuuid);
        log.info("<< GRANDMASTER ICON & LEVEL DONE >>");
        storeProfile(masterPuuid);
        log.info("<< MASTER ICON & LEVEL DONE >>");
    }


    private void storeProfile(List<String> puuids) throws InterruptedException {
        List<Summoner> list = new ArrayList<>();
        int i=1;
        for (String puuid : puuids) {
            Optional<Summoner> optionalSummoner = summonerService.getSummonerByPuuid(puuid);
            Summoner summoner = null;
            if (optionalSummoner.isEmpty()){ // 저장되어 있지 않는 사람이야
                RiotAccountDto newInform = rankerService.getNewSummonerInformation(puuid);
                if (newInform == null){
                    continue;
                }
                summoner = summonerService.saveSummoner(newInform);
            }
            else {
                summoner = optionalSummoner.get();
            }

            if (summoner.getIconId() == null || summoner.getLevel() == null){ // icon이나 level이 null이면
                log.info("{} --- {} : icon & level request ",i++, puuid);
                ProfileResDto profileResDto = requestProfileToRiot(puuid);
                summoner.updateProfile(profileResDto);
                list.add(summoner);
            }

        }
        summonerService.updateSummoners(list);
    }

    private ProfileResDto requestProfileToRiot(String puuid) throws InterruptedException {
        for (int i=0; i<3; i++){
            try{
                ProfileResDto profileInfo = riotApiService.getProfileInfo(puuid);
                if (profileInfo!=null) return profileInfo;
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
