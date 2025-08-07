package com.nexushub.NexusHub.Riot.Summoner.repository;

import com.nexushub.NexusHub.Riot.Summoner.domain.Summoner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SummonerRepository extends JpaRepository<Summoner, Long> {
    Optional<Summoner> findSummonerByTrimmedGameNameAndTagLine(String gameName, String tagLine);
    Optional<Summoner> findSummonerByPuuid(String puuid);
}
