package com.nexushub.NexusHub.Riot.Match.repository;

import com.nexushub.NexusHub.Riot.Match.domain.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    public Optional<Match> findMatchByMatchId(String matchId);

}
