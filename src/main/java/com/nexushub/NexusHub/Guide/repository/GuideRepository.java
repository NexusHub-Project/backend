package com.nexushub.NexusHub.Guide.repository;

import com.nexushub.NexusHub.InGame.Champion.Champion;
import org.springframework.data.jpa.repository.JpaRepository;
import com.nexushub.NexusHub.Guide.domain.Guide;

import java.util.List;

public interface GuideRepository extends JpaRepository<Guide, Long> {
    List<Guide> findByChampion(Champion champion);
}

