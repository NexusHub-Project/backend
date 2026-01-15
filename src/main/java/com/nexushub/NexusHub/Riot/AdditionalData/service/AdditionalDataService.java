package com.nexushub.NexusHub.Riot.AdditionalData.service;


import com.nexushub.NexusHub.Common.Exception.Fail.TooManyRequestFail;
import com.nexushub.NexusHub.Common.Exception.RiotAPI.CannotFoundSummoner;
import com.nexushub.NexusHub.Riot.AdditionalData.dto.IconAndLevel;
import com.nexushub.NexusHub.Riot.Ranker.domain.Tier;
import com.nexushub.NexusHub.Riot.Ranker.dto.FromRiotRankerResDto;
import com.nexushub.NexusHub.Riot.Ranker.dto.RiotRankerDto;
import com.nexushub.NexusHub.Riot.RiotInform.dto.ProfileResDto;
import com.nexushub.NexusHub.Riot.RiotInform.service.RiotApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdditionalDataService {

    private final RiotApiService riotApiService;

    @Value("${riot.api-key}")
    private String myKey;

    @Value("${riot.api-key-temp1}")
    private String apiKey1;

    @Value("${riot.api-key-temp2}")
    private String apiKey2;

    @Value("${riot.api-key-temp3}")
    private String apiKey3;

    @Value("${riot.api-key-temp4}")
    private String apiKey4;

    public void getDivision() throws CannotFoundSummoner {
        List<IconAndLevel> sortedIconAndLevel = getSortedIconAndLevel(myKey);
        log.info("size : {}", sortedIconAndLevel.size());
        List<IconAndLevel> sortedIconAndLevel1 = getSortedIconAndLevel(apiKey1);
        log.info("1) size : {}", sortedIconAndLevel1.size());
        List<IconAndLevel> sortedIconAndLevel2 = getSortedIconAndLevel(apiKey2);
        log.info("2) size : {}", sortedIconAndLevel2.size());
        List<IconAndLevel> sortedIconAndLevel3 = getSortedIconAndLevel(apiKey3);
        log.info("3) size : {}", sortedIconAndLevel3.size());
        List<IconAndLevel> sortedIconAndLevel4 = getSortedIconAndLevel(apiKey4);
        log.info("4) size : {}", sortedIconAndLevel4.size());
    }



    private List<IconAndLevel> getSortedIconAndLevel(String apiKey) throws CannotFoundSummoner {
        // 1. 원래 나의 키로 데이터 가져오기


        // 2. origin 속의 Entry를 lp 순으로 나열을 해야 함 -> 여기는 모두 정렬이 되어 있는 상태임
        List<RiotRankerDto> originChallenger = sortByLPFirst(riotApiService.getRankersByTierAndKey(Tier.CHALLENGER, apiKey));
        List<RiotRankerDto> originGrandMaster = sortByLPFirst(riotApiService.getRankersByTierAndKey(Tier.GRANDMASTER, apiKey));
        List<RiotRankerDto> originMaster = sortByLPFirst(riotApiService.getRankersByTierAndKey(Tier.MASTER, apiKey));

        List<IconAndLevel> origin = new ArrayList<>();
        int rank = 1;
        for (RiotRankerDto riotRankerDto : originChallenger) {
            origin.add(new IconAndLevel(rank++, riotRankerDto.getPuuid(), riotRankerDto.getLeaguePoints()));
        }
        for (RiotRankerDto riotRankerDto : originGrandMaster) {
            origin.add(new IconAndLevel(rank++, riotRankerDto.getPuuid(), riotRankerDto.getLeaguePoints()));
        }
        for (RiotRankerDto riotRankerDto : originMaster) {
            origin.add(new IconAndLevel(rank++, riotRankerDto.getPuuid(), riotRankerDto.getLeaguePoints()));
        }

        return origin;

    }
    private List<RiotRankerDto> sortByLPFirst(FromRiotRankerResDto origin){

        List<RiotRankerDto> list = origin.getEntries()
                .stream()
                .sorted((a, b) -> b.getLeaguePoints() - a.getLeaguePoints()).toList();

        return list;
    }

}
