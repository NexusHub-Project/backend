package com.nexushub.NexusHub.Summoner.service;

import com.nexushub.NexusHub.Exception.RiotAPI.CannotFoundSummoner;
import com.nexushub.NexusHub.InGame.Champion.Champion;
import com.nexushub.NexusHub.InGame.Champion.ChampionRepository;
import com.nexushub.NexusHub.Match.domain.Match;
import com.nexushub.NexusHub.Match.domain.MatchParticipant;
import com.nexushub.NexusHub.Match.dto.InfoDto;
import com.nexushub.NexusHub.Match.dto.MatchDto;
import com.nexushub.NexusHub.Match.dto.ParticipantDto;
import com.nexushub.NexusHub.Match.dto.v2.MatchDataDto;
import com.nexushub.NexusHub.Match.repository.MatchRepository;
import com.nexushub.NexusHub.Match.service.MatchService;
import com.nexushub.NexusHub.Riot.dto.MasteryDto;
import com.nexushub.NexusHub.Riot.dto.RiotAccountDto;
import com.nexushub.NexusHub.Riot.service.RiotApiService;
import com.nexushub.NexusHub.Summoner.domain.Summoner;
import com.nexushub.NexusHub.Summoner.dto.SummonerDto;
import com.nexushub.NexusHub.Summoner.dto.SummonerRequestDto;
import com.nexushub.NexusHub.Summoner.repository.SummonerRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SummonerService {
    private final SummonerRepository summonerRepository;
    private final RiotApiService riotApiService;
    private final ChampionRepository championRepository;
    private final MatchService matchService;


    public Summoner getSummonerTierInfoV1(SummonerRequestDto dto) throws CannotFoundSummoner {
        log.info("티어 찾기 2) : {}", dto);
        String puuid;
        SummonerDto summonerDto;

        if (dto.getSummonerId() == null){
            log.info("티어 찾기 3) : {}", dto);
            RiotAccountDto summonerInfo = riotApiService.getSummonerInfo(dto.getGameName(), dto.getTagLine());
            log.info("티어 찾기 4) : {}", summonerInfo);
            puuid = summonerInfo.getPuuid();
            summonerDto = SummonerDto.setInform(dto.getGameName(), dto.getTagLine(), puuid);
            riotApiService.getSummonerTierInfo(summonerDto);

            Summoner sum = Summoner.update(summonerDto);
            return summonerRepository.save(sum);
        }
        else {
            Summoner summoner = summonerRepository.findById(dto.getSummonerId()).get();
            puuid = summoner.getPuuid();
            summonerDto = SummonerDto.setInform(dto.getGameName(), dto.getTagLine(), puuid);
            riotApiService.getSummonerTierInfo(summonerDto);
            summoner.updateTier(summonerDto);
            return summoner;
        }
    }

    public Summoner getSummonerTierInfoV2(SummonerDto.Request dto) throws CannotFoundSummoner {
        //1. 일단 gameName + tagLine으로 찾아보기
        Optional<Summoner> summoner = summonerRepository.findSummonerByGameNameAndTagLine(dto.getGameName(), dto.getTagLine());

        //2. PUUID를 얻기 - 객체가 있을 수도 있고(최초 검색X) 없을 수도 있음(최초 검색O)
        //         객체가 있으면 그냥 바로 PUUID 뽑아오기
        //         객체가 없으면 PUUID를 얻어오기
        String puuid = summoner.isPresent() ? summoner.get().getPuuid() : riotApiService.getSummonerPuuid(dto.getGameName(), dto.getTagLine());
        //3. PUUID를 통해서 티어 검색하기
        SummonerDto tierInfo = riotApiService.getSummonerTierInfo(SummonerDto.setInform(dto.getGameName(), dto.getTagLine(), puuid));

        //4. Summoner 객체 적용하여 반환하기
        return this.SaveOrUpateSummoner(tierInfo, summoner);
    }

    public List<MasteryDto> getSummonerMasteryInfo(SummonerRequestDto dto) throws CannotFoundSummoner {
         //1. 일단 gameName + tagLine으로 찾아보기
         Optional<Summoner> summoner = summonerRepository.findSummonerByGameNameAndTagLine(dto.getGameName(), dto.getTagLine());

         //2. PUUID를 얻기 - 객체가 있을 수도 있고(최초 검색X) 없을 수도 있음(최초 검색O)
         //         객체가 있으면 그냥 바로 PUUID 뽑아오기
         //         객체가 없으면 PUUID를 얻어오기
         String puuid = summoner.isPresent() ? summoner.get().getPuuid() : riotApiService.getSummonerPuuid(dto.getGameName(), dto.getTagLine());

         List<MasteryDto> masteryInfo = riotApiService.getMasteryInfo(SummonerDto.setInform(dto.getGameName(), dto.getTagLine(), puuid));

         setChampionNameV2(masteryInfo);

         return masteryInfo;
    }

    public String[] getSummonerMatchesId(SummonerRequestDto dto) throws CannotFoundSummoner {
        TempInfo temp = getPuuid(dto);
        dto.setPuuid(temp.getPuuid());
        return riotApiService.getSummonerMatches(SummonerDto.setInform(temp.gameName, temp.tagLine, temp.puuid));
    }

    public List<MatchDto> getSummonerMatchesInfo(SummonerRequestDto dto) throws CannotFoundSummoner {
        String[] summonerMatchesId = getSummonerMatchesId(dto);
        List<MatchDto> dtos = new ArrayList<>();
        for (String matchId : summonerMatchesId) {
            dtos.add(matchService.getMatchInfo(matchId, dto.getPuuid()));
        }
        return dtos;
    }

    public List<MatchDataDto> getSummonerMatchesInfoV1(SummonerRequestDto dto) throws CannotFoundSummoner {
        // step 2) : dto에 담겨 있는 gameName, tagLine를 통해서 해당 유저의 MatchId들을 받아오기
        String[] summonerMatchesId = getSummonerMatchesId(dto);
        List<MatchDataDto> matchDataDtos = new ArrayList<>();

        // step 3) : matchId를 통해서 Match_info 객체를 받아오기  => 있을 수도 있고 없을 수도 있음
        for (String matchId : summonerMatchesId) {
            Optional<Match> match = matchService.getMatchByMatchId(matchId);

            // step 4) : match가 있다면 바로 matchDataDto 구성하기
            if (match.isPresent()) {
                // step 4-1) : MatchDataDto에는 player01~10까지 넣기
                List<MatchParticipant> participants = match.get().getParticipants();
                MatchDataDto matchDataDto = MatchDataDto.of(participants);
                matchDataDto.setMatchInform(match.get());
                // step 4-2) : MatchDataDto 객체를 List에 넣어준다
                matchDataDtos.add(matchDataDto);
            }

            // step 5) : match가 없다면 새로 만들어서 matchDataDto를 구성하기
            else {
                // step 5-1) : riot API 요청을 통해서 해당 matchId의 값을 받기
                MatchDto matchDto = riotApiService.getMatchInfo(matchId);

                // step 5-2) : matchDto 속의 infoDto를 통해서 participantDto를 통해, Summoner에 저장이 되어 있는 Summoner인지 체크하기
                InfoDto infoDto = matchDto.getInfo();
                List<ParticipantDto> participantsDtoFromApi = infoDto.getParticipants();


                // 아직 저장 안 함
                Match newMatch = Match.builder()
                        .matchId(matchId) // Riot API에서 받은 matchId
                        .gameMode(infoDto.getGameMode())
                        .gameDuration(infoDto.getGameDuration())
                        .gameCreation(infoDto.getGameCreation())
                        .gameEndTimestamp(infoDto.getGameEndTimestamp())
                        .build();

                List<MatchParticipant> matchParticipants = new ArrayList<>();

                for (ParticipantDto participantDto : participantsDtoFromApi) {
                    // step 5-3) : puuid로 Summoner를 찾거나, 없으면 새로 저장합니다.
                    Summoner summoner = summonerRepository.findSummonerByPuuid(participantDto.getPuuid())
                            .orElseGet(() -> summonerRepository.save(
                                    new Summoner(participantDto.getRiotIdGameName(), participantDto.getRiotIdTagline(), participantDto.getPuuid())
                            ));

                    MatchParticipant participant = MatchParticipant.builder()
                            .match(newMatch)
                            .summoner(summoner)
                            .win(participantDto.getWin())
                            .championId(participantDto.getChampionId())
                            .champLevel(participantDto.getChampLevel())
                            .teamPosition(participantDto.getTeamPosition())
                            .item0(participantDto.getItem0())
                            .item1(participantDto.getItem1())
                            .item2(participantDto.getItem2())
                            .item3(participantDto.getItem3())
                            .item4(participantDto.getItem4())
                            .item5(participantDto.getItem5())
                            .item6(participantDto.getItem6())
                            .perks(participantDto.getPerks())
                            .kda(participantDto.getKda())
                            .kills(participantDto.getKills())
                            .assists(participantDto.getAssists())
                            .deaths(participantDto.getDeaths())
                            .totalMinionKills(participantDto.getTotalMinionsKilled() + participantDto.getNeutralMinionsKilled())
                            .totalDamageTaken(participantDto.getTotalDamageTaken())
                            .totalDamageDealtToChampions(participantDto.getTotalDamageDealtToChampions())
                            .doubleKills(participantDto.getDoubleKills())
                            .tripleKills(participantDto.getTripleKills())
                            .quadraKills(participantDto.getQuadraKills())
                            .pentaKills(participantDto.getPentaKills())
                            .build();

                    participant.setTeamLuckScore(ThreadLocalRandom.current().nextInt(35, 100));
                    participant.setOurScore(ThreadLocalRandom.current().nextInt(35, 100));
                    matchParticipants.add(participant);
                }

                newMatch.setParticipants(matchParticipants);

                MatchDataDto matchDataDto = MatchDataDto.of(matchParticipants);
                matchDataDto.setMatchInform(newMatch);
                matchDataDtos.add(matchDataDto);

                matchService.save(newMatch);
            }
        }

        return matchDataDtos;
    }

    private Summoner SaveOrUpateSummoner(SummonerDto dto, Optional<Summoner> summoner){
        if (summoner.isPresent()){ // 최초 검색이 아닌 경우
            Summoner target = summoner.get();
            target.updateTier(dto);
            return summonerRepository.save(target);
        }
        else { // 최초 검색인 경우
            Summoner target = Summoner.update(dto);
            return summonerRepository.save(target);
        }
    }


    private List<MasteryDto> setChampionNameV1(List<MasteryDto> dtos){
        for (MasteryDto dto : dtos) {
            Champion champion = championRepository.findById(dto.getChampionId()).get();
            log.info("champId : {}, ChampName : {}",dto.getChampionId(), dto.getChampionName());
        }
        return dtos;
    }

    private List<MasteryDto> setChampionNameV2(List<MasteryDto> dtos) {
        for (MasteryDto dto : dtos) {
            // 1. championId로 Champion 정보를 조회합니다.
            Optional<Champion> championOptional = championRepository.findById(dto.getChampionId());

            // 2. ifPresent를 사용하여 Optional 객체가 비어있지 않은 경우에만 내부 로직을 실행합니다.
            //    이렇게 하면 데이터가 없을 때 .get()으로 인한 오류를 원천적으로 방지할 수 있습니다.
            championOptional.ifPresent(champion -> {
                // 3. (핵심) 조회한 champion 객체의 이름을 MasteryDto에 설정합니다.
                dto.setChampionName(champion.getNameKo());
                log.info("매핑 성공 - champId: {}, ChampName: {}", dto.getChampionId(), dto.getChampionName());
            });

            // 4. (선택사항) 만약 데이터가 없는 경우를 확인하고 싶다면 아래와 같이 처리할 수 있습니다.
            if (championOptional.isEmpty()) {
                log.warn("DB에서 Champion 정보를 찾을 수 없습니다. champId: {}", dto.getChampionId());
            }
        }
        return dtos;
    }

    private TempInfo getPuuid(SummonerRequestDto dto) throws CannotFoundSummoner {
        //1. 일단 gameName + tagLine으로 찾아보기
        Optional<Summoner> summoner = summonerRepository.findSummonerByGameNameAndTagLine(dto.getGameName(), dto.getTagLine());

        //2. PUUID를 얻기 - 객체가 있을 수도 있고(최초 검색X) 없을 수도 있음(최초 검색O)
        //         객체가 있으면 그냥 바로 PUUID 뽑아오기
        //         객체가 없으면 PUUID를 얻어오기
        String puuid = summoner.isPresent() ? summoner.get().getPuuid() : riotApiService.getSummonerPuuid(dto.getGameName(), dto.getTagLine());

        return new TempInfo(puuid, dto.getGameName(), dto.getTagLine());
    }

    @Data
    @AllArgsConstructor
    private class TempInfo{
        private String puuid;
        private String gameName;
        private String tagLine;
    }
}