package com.nexushub.NexusHub.Riot.Summoner.service;

import com.nexushub.NexusHub.Common.Exception.RiotAPI.CannotFoundSummoner;
import com.nexushub.NexusHub.Riot.Data.Champion.Champion;
import com.nexushub.NexusHub.Riot.Data.Champion.ChampionRepository;
import com.nexushub.NexusHub.Riot.Match.domain.Match;
import com.nexushub.NexusHub.Riot.Match.domain.MatchParticipant;
import com.nexushub.NexusHub.Riot.Match.dto.InfoDto;
import com.nexushub.NexusHub.Riot.Match.dto.MatchDto;
import com.nexushub.NexusHub.Riot.Match.dto.ParticipantDto;
import com.nexushub.NexusHub.Riot.Match.dto.v2.MatchDataDto;
import com.nexushub.NexusHub.Riot.Match.dto.v3.MatchInfoResDto;
import com.nexushub.NexusHub.Riot.Match.dto.v3.MetaDataResDto;
import com.nexushub.NexusHub.Riot.Match.dto.v3.MyDataResDto;
import com.nexushub.NexusHub.Riot.Match.dto.v3.ParticipantsResDto;
import com.nexushub.NexusHub.Riot.Match.service.MatchService;
import com.nexushub.NexusHub.Riot.RiotInform.dto.MasteryDto;
import com.nexushub.NexusHub.Riot.RiotInform.dto.Ranker.ChallengerDto;
import com.nexushub.NexusHub.Riot.RiotInform.dto.Ranker.ChallengerLeagueDto;
import com.nexushub.NexusHub.Riot.RiotInform.dto.Ranker.ChallengersResDto;
import com.nexushub.NexusHub.Riot.RiotInform.dto.RiotAccountDto;
import com.nexushub.NexusHub.Riot.RiotInform.service.RiotApiService;
import com.nexushub.NexusHub.Riot.Summoner.domain.Summoner;
import com.nexushub.NexusHub.Riot.Summoner.dto.SummonerDto;
import com.nexushub.NexusHub.Riot.Summoner.dto.SummonerTierResDto;
import com.nexushub.NexusHub.Riot.Summoner.repository.SummonerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

    /** gameName, tagLine으로 Optional<Summoner>를 반환하는 메소드
     *
     * @param gameName
     * @param tagLine
     * @return
     */
    @Transactional(readOnly = true)
    public Optional<Summoner> findSummoner(String gameName, String tagLine) {
        return summonerRepository.findSummonerByTrimmedGameNameAndTagLine(gameName.replace(" ", ""), tagLine);
    }
    /** 최근 전적 MatchId를 반환하는 메소드
     *
     * @param gameName, tagLine
     * @return
     * @throws CannotFoundSummoner
     */
    private String[] getSummonerMatchesId(String gameName, String tagLine) throws CannotFoundSummoner {
        Optional<Summoner> summoner = this.findSummoner(gameName, tagLine);
        return riotApiService.getSummonerMatches(SummonerDto.setInform(gameName, tagLine, findPuuid(gameName, tagLine, summoner)));
    }

    /** gameName, tagLine, Optional<Summoner>로 puuid 반환하는 메소드
     *
     * @param gameName
     * @param tagLine
     * @param summoner
     * @return
     * @throws CannotFoundSummoner
     */
    public String findPuuid(String gameName, String tagLine, Optional<Summoner> summoner) throws CannotFoundSummoner {
        return summoner.isPresent()
                ? summoner.get().getPuuid()
                : riotApiService.getSummonerInfo(gameName, tagLine).getPuuid();
    }


    /** gameName, tagLine을 통해서 티어 정보를 반한 하는 메소드
     *
     * @param gameName
     * @param tagLine
     * @return
     * @throws CannotFoundSummoner
     */
    public SummonerTierResDto getSummonerTierInfo(String gameName, String tagLine) throws CannotFoundSummoner {
        log.info("SummonerService - getSummonerTierInfo : {}#{}", gameName, tagLine);
        Optional<Summoner> summoner = findSummoner(gameName, tagLine);
        String puuid = findPuuid(gameName, tagLine, summoner);

        SummonerDto tierInfo = riotApiService.getSummonerTierInfo(SummonerDto.setInform(gameName, tagLine, puuid));

        log.info("문제 예상 1)");
        Summoner savedS = this.SaveOrUpateSummoner(tierInfo, summoner);
        return SummonerTierResDto.of(savedS);
    }

    /** gameName, tagLine을 통해서 숙련도 리스트를 반환하는 메소드
     *
     * @param gameName
     * @param tagLine
     * @return
     * @throws CannotFoundSummoner
     */
    public List<MasteryDto> getSummonerMasteryInfo(String gameName, String tagLine) throws CannotFoundSummoner {
        log.info("Summoner Service - getSummonerMasteryInfo : {}#{}", gameName, tagLine);

        Optional<Summoner> summoner = findSummoner(gameName, tagLine);
        String puuid = findPuuid(gameName, tagLine, summoner);

        List<MasteryDto> masteryInfo = riotApiService.getMasteryInfo(puuid);

        setChampionName(masteryInfo);

        return masteryInfo;
    }


    /** gameName, tagLine을 통해서 전적을 검색 내용을 반환하는 메소드
     *
     * @param gameName
     * @param tagLine
     * @return
     * @throws CannotFoundSummoner
     */
    public Queue<MatchInfoResDto> getSummonerMatches(String gameName, String tagLine) throws CannotFoundSummoner {
        log.info("Summoner Service - getSummonerMatches : {}#{}", gameName, tagLine);
        Queue<MatchInfoResDto> matchInfoResDtos = new LinkedList<>();

        Optional<Summoner> s = this.findSummoner(gameName, tagLine);
        String puuid = this.findPuuid(gameName, tagLine,s);

        String[] summonerMatchesId = getSummonerMatchesId(gameName, tagLine);


        // step 1) : matchId를 통해서 Match_info 객체를 받아오기  => 있을 수도 있고 없을 수도 있음
        for (String matchId : summonerMatchesId) {
            log.info("match Id : {}", matchId);
            Optional<Match> match = matchService.getMatchByMatchId(matchId);

            // step 2-1) : match가 있다면 바로 matchDataDto 구성하기
            if (match.isPresent()) {
                log.info("{} 있음 ", matchId);
/*
                // step 3-1) : MatchDataDto에는 player01~10까지 넣기
                List<MatchParticipant> participants = match.get().getParticipants();
                MatchDataDto matchDataDto = MatchDataDto.of(participants);
                matchDataDto.setMatchInform(match.get());
                // step 4-1) : MatchDataDto 객체를 List에 넣어준다
                matchDataDtos.add(matchDataDto);
*/
                Match match1 = match.get();
                ParticipantsResDto participantsResDto = ParticipantsResDto.of(match1.getParticipants());
                MetaDataResDto metaDataResDto = MetaDataResDto.of(match1);
                log.info("queueID : {}", match1.getQueueId());
                log.info("queueId : {}", metaDataResDto.getQueueId());
                MatchParticipant myDataByPuuid = match1.getMyDataByPuuid(puuid);
                MyDataResDto myDataResDto = MyDataResDto.of(myDataByPuuid);
                myDataResDto.setPerks(myDataByPuuid);
                // step 4-2) : MatchDataDto 객체를 List에 넣어준다
                MatchInfoResDto dto = MatchInfoResDto.of(metaDataResDto, myDataResDto, participantsResDto);
                log.info("queueIOD : {}", dto.getMetaData().getQueueId());
                matchInfoResDtos.add(dto);
            }
            // step 2-2) : match가 없다면 만들고 matchDataDto 구성하기
            else {
                log.info("{} 없음 ", matchId);

                // step 3-2) : riot API 요청을 통해서 해당 matchId의 값을 받기
                MatchDto matchDto = riotApiService.getMatchInfo(matchId);

                // step 4-2) : matchDto 속의 infoDto를 통해서 participantDto를 통해, Summoner에 저장이 되어 있는 Summoner인지 체크하기
                InfoDto infoDto = matchDto.getInfo();
                List<ParticipantDto> participantsDtoFromApi = infoDto.getParticipants(); // 참가자 정보 찾아 왔음


                //여기서 queueID 넣어야 함
                // 기본 정보 저장
                Match newMatch = Match.builder()
                        .matchId(matchId) // Riot API에서 받은 matchId
                        .gameMode(infoDto.getGameMode())
                        .gameDuration(infoDto.getGameDuration())
                        .gameCreation(infoDto.getGameCreation())
                        .gameEndTimestamp(infoDto.getGameEndTimestamp())
                        .queueId(infoDto.getQueueId())
                        .build();

                List<MatchParticipant> matchParticipants = new ArrayList<>();

                for (ParticipantDto participantDto : participantsDtoFromApi) {
                    // step 5-2) : puuid로 Summoner를 찾거나, 없으면 새로 저장
                    Summoner summoner = summonerRepository.findSummonerByPuuid(participantDto.getPuuid())
                            .orElseGet(() -> summonerRepository.save(
                                    new Summoner(participantDto)
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

                ParticipantsResDto participantsResDto = ParticipantsResDto.of(matchParticipants);
                MetaDataResDto metaDataResDto = MetaDataResDto.of(newMatch);
                MatchParticipant myDataByPuuid = newMatch.getMyDataByPuuid(puuid);
                MyDataResDto myDataResDto = MyDataResDto.of(myDataByPuuid);
                myDataResDto.setPerks(myDataByPuuid);

                matchInfoResDtos.add(MatchInfoResDto.of(metaDataResDto, myDataResDto, participantsResDto));
                matchService.save(newMatch);
            }
        }

        return matchInfoResDtos;
    }

    /** 챌린저 dto를 통해서 해당 유저를 저장하고 ResponseDTo로 변환하는 메소드
     *
     * @param dto
     * @return
     */
    public List<ChallengersResDto> setChallengersData(ChallengerLeagueDto dto) {
        List<ChallengerDto> entries = dto.getEntries();
        List<ChallengersResDto> dtos = new LinkedList<>();
        for (ChallengerDto entry : entries) {
            Optional<Summoner> summoner = summonerRepository.findSummonerByPuuid(entry.getPuuid());

            if (summoner.isPresent()) {
                dtos.add(new ChallengersResDto(entry, summoner.get()));
            }
            else {
                // 1. puuid만으로 정보 얻어오기
                RiotAccountDto riotAccountInfo = riotApiService.getRiotAccountInfo(entry.getPuuid());

                // 2. 그거 summoner에 저장하기
                Summoner newSummoner = new Summoner(riotAccountInfo.getGameName(), riotAccountInfo.getTagLine(), riotAccountInfo.getPuuid());
                newSummoner.updateTier(entry);

                // 3. 그리고 바로 Dtos.add 해버기리
                dtos.add(new ChallengersResDto(entry,summonerRepository.save(newSummoner)));
            }
        }
        return dtos;
    }

    /** Summoner 객체를 저장과 업데이트를 하는 메소드
     *
     * @param dto
     * @param summoner
     * @return
     */
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


    /** 숙련도 할 때 세팅하는 메소드
     *
     * @param dtos
     * @return
     */
    private List<MasteryDto> setChampionName(List<MasteryDto> dtos) {
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


}