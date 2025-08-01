package com.nexushub.NexusHub.Riot.service;

import com.nexushub.NexusHub.Exception.RiotAPI.CannotFoundSummoner;
import com.nexushub.NexusHub.Match.dto.*;
import com.nexushub.NexusHub.Match.dto.minimal.MinimalMatchDto;
import com.nexushub.NexusHub.Riot.dto.*;
import com.nexushub.NexusHub.Riot.dto.Ranker.ChallengerLeagueDto;
import com.nexushub.NexusHub.Summoner.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiotApiService {
    @Value("${riot.api-key}")
    private String apiKey;

    private String baseUrlAsia = "https://asia.api.riotgames.com";
    private String baseUrlKR = "https://kr.api.riotgames.com";

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

    public SummonerDto getSummonerTierInfo(SummonerDto dto){
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
            for (TierInfoDto tierInfoDto : tierInfoDtos) {
                log.info(tierInfoDto.toString());
            }
            return setSummonerDto(dto, tierInfoDtos);
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
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }
    public List<MasteryDto> getMasteryInfo(SummonerDto dto) throws CannotFoundSummoner {
        String url = baseUrlKR + "/lol/champion-mastery/v4/champion-masteries/by-puuid/" + dto.getPuuid();

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



}
