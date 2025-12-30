package com.nexushub.NexusHub.Riot.Ranker.repository;

import com.nexushub.NexusHub.Riot.Ranker.domain.Ranker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankerRepository extends JpaRepository<Ranker, Long> {
    // 랭커 전체 데이터 삭제 (새로운 시즌/갱신 시 필요할 경우)
    void deleteAll();
}