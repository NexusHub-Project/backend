package com.nexushub.NexusHub.Match.repository;

import com.nexushub.NexusHub.Match.domain.MatchParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchParticipantRepository extends JpaRepository<MatchParticipant, Long> {
}
