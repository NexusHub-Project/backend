package com.nexushub.NexusHub.Riot.Ranker.service;

import com.nexushub.NexusHub.Riot.Ranker.domain.Ranker;
import com.nexushub.NexusHub.Riot.Ranker.domain.Tier;
import com.nexushub.NexusHub.Riot.Ranker.dto.FromRiotRankerResDto;
import com.nexushub.NexusHub.Riot.Ranker.dto.RiotRankerDto;
import com.nexushub.NexusHub.Riot.Ranker.repository.RankerRepository;
import com.nexushub.NexusHub.Riot.RiotInform.service.RiotApiService;
import com.nexushub.NexusHub.Riot.Summoner.domain.Summoner;
import com.nexushub.NexusHub.Riot.Summoner.dto.SummonerDto;
import com.nexushub.NexusHub.Riot.Summoner.repository.SummonerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankerService {

    private final RiotApiService riotApiService;
    private final RankerRepository rankerRepository;
    private final SummonerRepository summonerRepository;

    @Transactional
    public void refreshRankerData() {
        log.info("🚀 랭커 데이터 갱신 시작...");

        // 1. 기존 데이터 초기화
        rankerRepository.deleteAllInBatch();

        // 2. 티어별 데이터 조회 및 저장 (Challenger, GM, Master)
        // getLeagueByTier 메서드는 429 에러 시 자동 대기하므로 안전합니다.
        processLeague(riotApiService.getLeagueByTier(Tier.CHALLENGER), Tier.CHALLENGER);
        processLeague(riotApiService.getLeagueByTier(Tier.GRANDMASTER), Tier.GRANDMASTER);

        // *마스터 티어는 인원이 많아 시간이 오래 걸릴 수 있음 (필요 시 주석 해제)
        // processLeague(riotApiService.getLeagueByTier(Tier.MASTER), Tier.MASTER);

        log.info("✅ 랭커 데이터 갱신 완료!");
    }

    private void processLeague(FromRiotRankerResDto leagueDto, Tier tier) {
        if (leagueDto == null || leagueDto.getEntries() == null) return;

        List<RiotRankerDto> entries = leagueDto.getEntries();

        // LP 높은 순으로 정렬
        entries.sort(Comparator.comparingInt(RiotRankerDto::getLeaguePoints).reversed());

        int rank = 1;
        for (RiotRankerDto entry : entries) {
            try {
                saveRanker(entry, tier, rank++);

                if (rank % 50 == 0) log.info("[{}] {}위 처리 중...", tier, rank);
            } catch (Exception e) {
                // 한 명 실패해도 멈추지 않고 계속 진행
                log.error("랭커 저장 실패 (Tier: {}, ID: {}): {}", tier, entry.getPuuid(), e.getMessage());
            }
        }
    }

    private void saveRanker(RiotRankerDto entry, Tier tier, int rank) {
        // [주의] RiotRankerDto의 puuid 필드에는 실제로는 'summonerId'가 들어있음 (@JsonProperty 때문)
        String encryptedSummonerId = entry.getPuuid();

        // 1. API로 상세 정보 조회 (여기서 진짜 PUUID와 GameName을 얻음)
        //    *API 호출이 많아 429가 발생할 수 있으나 RiotApiService가 처리함
        SummonerDto summonerDto = riotApiService.getSummonerBySummonerId(encryptedSummonerId);

        // 2. 소환사 DB 조회 또는 생성 (Upsert)
        Summoner summoner = summonerRepository.findSummonerByPuuid(summonerDto.getPuuid())
                .orElseGet(() -> {
                    Summoner newSummoner = Summoner.builder()
                            .puuid(summonerDto.getPuuid())
                            .gameName(summonerDto.getGameName())
                            .tagLine(summonerDto.getTagLine())
                            .trimmedGameName(summonerDto.getGameName() != null ?
                                    summonerDto.getGameName().replace(" ", "") : "")
                            .build();
                    return summonerRepository.save(newSummoner);
                });

        // 3. 소환사 티어/승패 정보 업데이트
        // (Summoner 엔티티에 updateTier(RiotRankerDto) 메서드 필요)
        summoner.updateTier(entry);

        // 4. Ranker 테이블에 저장
        Ranker ranker = Ranker.builder()
                .summoner(summoner)
                .tier(tier)
                .leaguePoint(entry.getLeaguePoints())
                .ranking(rank)
                .build();

        rankerRepository.save(ranker);
    }
}