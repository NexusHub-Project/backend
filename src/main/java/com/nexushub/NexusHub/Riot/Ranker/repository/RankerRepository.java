package com.nexushub.NexusHub.Riot.Ranker.repository;

import com.nexushub.NexusHub.Riot.Ranker.domain.Ranker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankerRepository extends JpaRepository<Ranker, Long> {
}