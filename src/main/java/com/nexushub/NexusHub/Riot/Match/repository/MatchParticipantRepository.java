package com.nexushub.NexusHub.Riot.Match.repository;

import com.nexushub.NexusHub.Riot.Match.domain.MatchParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchParticipantRepository extends JpaRepository<MatchParticipant, Long> {
}
