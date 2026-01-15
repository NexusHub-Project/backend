package com.nexushub.NexusHub.Riot.RiotInform.service;

import com.nexushub.NexusHub.Common.Exception.Fail.*;
import com.nexushub.NexusHub.Common.Exception.RiotAPI.CannotFoundSummoner;
import com.nexushub.NexusHub.Riot.Match.dto.MatchDto;
import com.nexushub.NexusHub.Riot.Match.dto.minimal.MinimalMatchDto;
import com.nexushub.NexusHub.Riot.Ranker.domain.Tier;
import com.nexushub.NexusHub.Riot.Ranker.dto.FromRiotRankerResDto;
import com.nexushub.NexusHub.Riot.Ranker.dto.RiotRankerDto;
import com.nexushub.NexusHub.Riot.RiotInform.dto.*;
import com.nexushub.NexusHub.Riot.RiotInform.dto.Ranker.ChallengerLeagueDto;
import com.nexushub.NexusHub.Riot.Summoner.dto.SummonerDto;
import com.zaxxer.hikari.util.IsolationLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.WebListenerRegistry;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiotApiService {
    private final WebListenerRegistry webListenerRegistry;
    @Value("${riot.api-key}")
    private String apiKey;

    private String baseUrlAsia = "https://asia.api.riotgames.com";
    private String baseUrlKR = "https://kr.api.riotgames.com";
    private static final String KR_BASE_URL = "https://kr.api.riotgames.com";

    private final RestTemplate restTemplate = new RestTemplate();

    public RiotAccountDto getSummonerInfo(String gameName, String tagLine) throws CannotFoundSummoner {
        // uuid 정보 얻기
        String url = baseUrlAsia + "/riot/account/v1/accounts/by-riot-id/" + gameName +"/" + tagLine;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", apiKey);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<RiotAccountDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    RiotAccountDto.class
            );
            RiotAccountDto body = response.getBody();
            log.info("RiotAPI SERVICE : RiotAccountDto : {}", body.toString());
            return response.getBody();

        } catch (HttpClientErrorException.NotFound e) {
            // 404 에러일 경우 직접 메시지 던짐
            throw new CannotFoundSummoner(gameName + "#" + tagLine + " 소환사를 찾을 수 없습니다.");
        } catch (RestClientException e) {
            log.error(" Riot API ERROR : {}", e.getMessage());
            throw new CannotFoundSummoner("소환사 정보를 가져오는 중 오류가 발생했습니다.");
        }
    }
    public String getSummonerPuuid(String gameName, String tagLine) throws CannotFoundSummoner {
        return getSummonerInfo(gameName, tagLine).getPuuid();
    }

    public ProfileResDto getProfileInfo(String puuid) throws CannotFoundSummoner {
        // uuid 정보 얻기
        String url = baseUrlKR + "/lol/summoner/v4/summoners/by-puuid/" + puuid;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", apiKey);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ProfileDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    ProfileDto.class
            );
            ProfileDto body = response.getBody();
            return ProfileResDto.of(body);

        } catch (HttpClientErrorException.NotFound e) {
            // 404 에러일 경우 직접 메시지 던짐
            throw new CannotFoundSummoner(puuid + " 소환사를 찾을 수 없습니다.");
        } catch (RestClientException e) {
            log.error(" Riot API ERROR : {}", e.getMessage());
            throw new CannotFoundSummoner("소환사 정보를 가져오는 중 오류가 발생했습니다.");
        }
    }

    public SummonerDto getSummonerTierInfo(SummonerDto dto){
        log.info("RiotApiService : dto : {}", dto.toString());
        String url = baseUrlKR + "/lol/league/v4/entries/by-puuid/"+dto.getPuuid() ;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", apiKey);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<TierInfoDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {
                    }
            );
            List<TierInfoDto> tierInfoDtos = response.getBody();
            log.info("tier INFORM : {}",tierInfoDtos.toString());
            return setSummonerDtoV2(dto, tierInfoDtos);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public RiotAccountDto getRiotAccountInfo(String puuid) {
        String url = baseUrlAsia + "/riot/account/v1/accounts/by-puuid/"+puuid;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", apiKey);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<RiotAccountDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {
                    }
            );
            RiotAccountDto body = response.getBody();
            log.info("riotAccountDto : {} {} {}", body.getGameName(), body.getTagLine(), body.getPuuid());
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("PUUID : {} 에 해당하는 소환사가 없다", puuid);
            return null;
        } catch (HttpClientErrorException.TooManyRequests e){
            log.warn("API 호출 LIMIT 초과");
            return null;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }
    public List<MasteryDto> getMasteryInfo(String puuid) throws CannotFoundSummoner {
        String url = baseUrlKR + "/lol/champion-mastery/v4/champion-masteries/by-puuid/" + puuid;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", apiKey);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<List<MasteryDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {
                    }
            );
            return response.getBody();
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }

    }
    public String[] getSummonerMatches(SummonerDto dto) throws CannotFoundSummoner {
        String url = baseUrlAsia + "/lol/match/v5/matches/by-puuid/"+dto.getPuuid()+"/ids";
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", apiKey);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        try{
            ResponseEntity<String[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {}
            );
            return response.getBody();
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public MatchDto getMatchInfo(String matchId) {
        String url = baseUrlAsia + "/lol/match/v5/matches/"+matchId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", apiKey);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        try{
            ResponseEntity<MatchDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public List<String> getMatchIdByPuuid(String puuid, long seasonStartTime){
        // 쿼리 파라미터로 검색 기간, 솔랭, 갯수 넣어서 가져옴 => 쿼리 파라미터 넣고 싶어서 UriComponentBuilder 쓰기로 함

        String url = UriComponentsBuilder.fromHttpUrl(baseUrlAsia + "/lol/match/v5/matches/by-puuid/" + puuid + "/ids")
                .queryParam("startTime", seasonStartTime) // 시즌 시작 시간 필터
                .queryParam("queue", 420) // 솔랭만 가져오기
                .queryParam("count", 100) // API 제한 고려하여 최대 100개씩 가져오기
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", apiKey);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try{
            ResponseEntity<List<String>> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("전적 ID 목록 조회 실패. puuid: {}, error: {}", puuid, e.getMessage());
            return new ArrayList<>(); // 오류 발생 시 빈 리스트 반환
        }
    }

    public MinimalMatchDto getMinimalMatchInfo(String matchId) {
        String url = baseUrlAsia + "/lol/match/v5/matches/" + matchId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", apiKey);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<MinimalMatchDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    MinimalMatchDto.class // 새로 만든 MinimalMatchDto로 파싱
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to get minimal match info for {}: {}", matchId, e.getMessage());
            return null;
        }
    }

    public ChallengerLeagueDto getChallengers(){
        String url = baseUrlKR + "/lol/league/v4/challengerleagues/by-queue/RANKED_SOLO_5x5";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", apiKey);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        try{
            ResponseEntity<ChallengerLeagueDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public FromRiotRankerResDto getChallengersV2(Tier tier) throws CannotFoundSummoner {
        String url = baseUrlKR;
        if (tier == Tier.CHALLENGER){
            url = url + "/lol/league/v4/challengerleagues/by-queue/RANKED_SOLO_5x5";
        }
        else if (tier == Tier.GRANDMASTER){
            url = url + "/lol/league/v4/grandmasterleagues/by-queue/RANKED_SOLO_5x5";
        }
        else if (tier == Tier.MASTER){
            url = url + "/lol/league/v4/masterleagues/by-queue/RANKED_SOLO_5x5";
        }
        else {
            log.warn("잘못된 랭크 티어 요청");
            throw new WrongRankTier("잘못된 랭크 티어 요청");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", apiKey);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        try{
            ResponseEntity<FromRiotRankerResDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {}
            );
            return response.getBody();
        }catch (HttpClientErrorException.TooManyRequests e) {
            // 429 에러 발생 시
            log.warn("API LIMIT 걸렸어");
            return null;
        } catch (Exception e) {
            // 다른 에러는 바로 던짐
            throw e;
        }
    }

    public FromRiotRankerResDto getRankersByTierAndKey(Tier tier, String key) throws CannotFoundSummoner {
        String url = baseUrlKR;
        if (tier == Tier.CHALLENGER){
            url = url + "/lol/league/v4/challengerleagues/by-queue/RANKED_SOLO_5x5";
        }
        else if (tier == Tier.GRANDMASTER){
            url = url + "/lol/league/v4/grandmasterleagues/by-queue/RANKED_SOLO_5x5";
        }
        else if (tier == Tier.MASTER){
            url = url + "/lol/league/v4/masterleagues/by-queue/RANKED_SOLO_5x5";
        }
        else {
            log.warn("잘못된 랭크 티어 요청");
            throw new WrongRankTier("잘못된 랭크 티어 요청");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", key);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        try{
            ResponseEntity<FromRiotRankerResDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {}
            );
            return response.getBody();
        }catch (HttpClientErrorException.TooManyRequests e) {
            // 429 에러 발생 시
            log.warn("API LIMIT 걸렸어");
            return null;
        } catch (Exception e) {
            // 다른 에러는 바로 던짐
            throw e;
        }
    }



    public PriorityQueue<RiotRankerDto> getRankersByKey(String key) throws CannotFoundSummoner {


        FromRiotRankerResDto challenger = getRankersByTierAndKey(Tier.CHALLENGER, key);
        if (challenger.getEntries().size() == 0 ){
            log.info("챌린저 랭킹 비어 있음");
        }
        FromRiotRankerResDto grandmaster = getRankersByTierAndKey(Tier.GRANDMASTER, key);
        if (grandmaster.getEntries().size()==0){
            log.info("그랜드마스터 랭킹 비어 있음");
        }
        FromRiotRankerResDto master = getRankersByTierAndKey(Tier.MASTER, key);
        if (master.getEntries().size()==0){
            log.info("마스터 랭킹 비어 있음");
        }
        PriorityQueue<RiotRankerDto> priorityQueue = new PriorityQueue<>(
                (a,b) -> b.getLeaguePoints() - a.getLeaguePoints()
        );

        for (RiotRankerDto entry : challenger.getEntries()) {
            priorityQueue.add(entry);
        }
        for (RiotRankerDto entry : grandmaster.getEntries()) {
            priorityQueue.add(entry);
        }
        for (RiotRankerDto entry : master.getEntries()) {
            priorityQueue.add(entry);
        }

        return priorityQueue;
    }







    private SummonerDto setSummonerDto(SummonerDto dto, List<TierInfoDto> list){
        if (list.size() == 2) {
            TierInfoDto flex = list.get(1);
            dto.setFlexRankDefeat(flex.getLosses());
            dto.setFlexRankWin(flex.getWins());
            dto.setFlexRankTier(flex.getTier()+" "+flex.getRank());
            dto.setFlexRankLP(flex.getLeaguePoints());
        }
        TierInfoDto sole = list.get(0);

        dto.setSoloRankDefeat(sole.getLosses());
        dto.setSoloRankWin(sole.getWins());
        dto.setSoloRankTier(sole.getTier()+" "+sole.getRank());
        dto.setSoloRankLP(sole.getLeaguePoints());

        return dto;
    }

    private SummonerDto setSummonerDtoV2(SummonerDto dto, List<TierInfoDto> list) {
        if (list.size() == 2) {
            TierInfoDto solo, flex;
            if (list.get(0).getQueueType().equals("RANKED_SOLO_5x5")) {
                solo = list.get(0);
                flex = list.get(1);
            } else {
                solo = list.get(1);
                flex = list.get(0);
            }
            dto.setSoloRankDefeat(solo.getLosses());
            dto.setSoloRankWin(solo.getWins());
            dto.setSoloRankTier(solo.getTier()+" "+solo.getRank());
            dto.setSoloRankLP(solo.getLeaguePoints());

            dto.setFlexRankDefeat(flex.getLosses());
            dto.setFlexRankWin(flex.getWins());
            dto.setFlexRankTier(flex.getTier()+" "+flex.getRank());
            dto.setFlexRankLP(flex.getLeaguePoints());
            return dto;
        } else if (list.size() == 1) {
            TierInfoDto temp = list.get(0);
            if (temp.getQueueType().equals("RANKED_SOLO_5x5")) {
                dto.setSoloRankDefeat(temp.getLosses());
                dto.setSoloRankWin(temp.getWins());
                dto.setSoloRankTier(temp.getTier() + " " + temp.getRank());
                dto.setSoloRankLP(temp.getLeaguePoints());
            } else {
                dto.setFlexRankDefeat(temp.getLosses());
                dto.setFlexRankWin(temp.getWins());
                dto.setFlexRankTier(temp.getTier()+" "+temp.getRank());
                dto.setFlexRankLP(temp.getLeaguePoints());
            }
            return dto;
        }


        return dto;
    }

    private <T> T callApiWithRetry(String url, Class<T> responseType) {
        int maxRetries = 5;
        int retryCount = 0;

        while (retryCount < maxRetries) {
            try {
                // API Key 추가 (이미 쿼리 파라미터가 있으면 & 없으면 ?)
                String requestUrl = url + (url.contains("?") ? "&" : "?") + "api_key=" + apiKey;
                return restTemplate.getForObject(requestUrl, responseType);

            } catch (HttpClientErrorException.TooManyRequests e) {
                retryCount++;
                String retryAfter = e.getResponseHeaders() != null ? e.getResponseHeaders().getFirst("Retry-After") : null;
                int sleepSeconds = (retryAfter != null && !retryAfter.isEmpty()) ? Integer.parseInt(retryAfter) : 10;

                log.warn("🚨 API 제한(429) 발생! {}초 대기 후 재시도... ({}/{})", sleepSeconds, retryCount, maxRetries);

                try {
                    Thread.sleep(sleepSeconds * 1000L + 1000); // 여유 있게 1초 추가 대기
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("API 재시도 중 인터럽트", ie);
                }
            } catch (Exception e) {
                log.error("API 호출 실패: url={}, error={}", url, e.getMessage());
                throw e; // 그 외 에러는 바로 던짐
            }
        }
        throw new RuntimeException("Riot API 재시도 횟수 초과");
    }

    /**
     * 티어별 랭킹 정보 조회 (Challenger, Grandmaster, Master)
     * 반환 타입: FromRiotRankerResDto
     */
    public FromRiotRankerResDto getLeagueByTier(Tier tier) {
        String url = KR_BASE_URL;
        if (tier == Tier.CHALLENGER) {
            url += "/lol/league/v4/challengerleagues/by-queue/RANKED_SOLO_5x5";
        } else if (tier == Tier.GRANDMASTER) {
            url += "/lol/league/v4/grandmasterleagues/by-queue/RANKED_SOLO_5x5";
        } else if (tier == Tier.MASTER) {
            url += "/lol/league/v4/masterleagues/by-queue/RANKED_SOLO_5x5";
        } else {
            throw new IllegalArgumentException("지원하지 않는 티어입니다: " + tier);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", apiKey);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<FromRiotRankerResDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    FromRiotRankerResDto.class
            );
            return response.getBody();

        } catch (HttpClientErrorException.TooManyRequests e){
            throw new TooManyRequestFail("Too Many Request AT Find Challenger Ranking");
        } catch (Exception e){
            log.warn(e.getMessage());
            return null;
        }

    }

    // puuid를 통해서 소환사 정보 획득하기
    public RiotAccountDto getSummonerByPuuid(String puuid) throws CannotFoundSummoner, TooManyRequestFail {
        // uuid 정보 얻기
        String url = baseUrlAsia + "/riot/account/v1/accounts/by-puuid/" + puuid;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", apiKey);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<RiotAccountDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    RiotAccountDto.class
            );
            RiotAccountDto body = response.getBody();
            return response.getBody();

        } catch (HttpClientErrorException.TooManyRequests e){
            log.info("GET Summoner Inform By Puuid");
            throw new TooManyRequestFail("Too Many Request At Find Summoner Inform By Puuid : "+ puuid);
        }
        catch (HttpClientErrorException.NotFound e) {
            // 404 에러일 경우 직접 메시지 던짐
            throw new CannotFoundSummoner(puuid + " 소환사를 찾을 수 없습니다.");
        } catch (RestClientException e) {
            log.error(" Riot API ERROR : {}", e.getMessage());
            throw new CannotFoundSummoner("소환사 정보를 가져오는 중 오류가 발생했습니다.");
        }
    }

    public ProfileResDto getProfileInfoByKeyAndPuuid(String key, String puuid) throws CannotFoundSummoner {

        if (key == null){
            throw new RiotAPIKeyException("key가 null입니다.");
        }
        else if (puuid == null){

            throw new RiotAPIKeyException("puuid가 null입니다.");
        }


        // uuid 정보 얻기
        String url = baseUrlKR + "/lol/summoner/v4/summoners/by-puuid/" + puuid;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", key);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ProfileDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    ProfileDto.class
            );
            ProfileDto body = response.getBody();
            return ProfileResDto.of(body);

        } catch (HttpClientErrorException.TooManyRequests e){
            throw new TooManyRequestFail("Too Many Request At Find Summoner Inform By Puuid : "+ puuid);
        } catch (HttpClientErrorException.NotFound e) {
            // 404 에러일 경우 직접 메시지 던짐
            throw new CannotFoundSummoner(puuid + " 소환사를 찾을 수 없습니다.");
        } catch (RestClientException e) {
            log.error(" Riot API ERROR : {}", e.getMessage());
            throw new CannotFoundSummoner("소환사 정보를 가져오는 중 오류가 발생했습니다.");
        }
    }
}
