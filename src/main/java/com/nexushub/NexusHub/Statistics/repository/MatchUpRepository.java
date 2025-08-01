package com.nexushub.NexusHub.Statistics.repository;

import com.nexushub.NexusHub.Statistics.domain.Champion.ChampionStatsByPosition;
import com.nexushub.NexusHub.Statistics.domain.MatchUp.MatchUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchUpRepository extends JpaRepository<MatchUp, Long> {
    List<MatchUp> findByOwnerStats(ChampionStatsByPosition stats);
}
