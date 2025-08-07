package com.nexushub.NexusHub.Web.Guide.repository;

import com.nexushub.NexusHub.Riot.Data.Champion.Champion;
import org.springframework.data.jpa.repository.JpaRepository;
import com.nexushub.NexusHub.Web.Guide.domain.Guide;

import java.util.List;

public interface GuideRepository extends JpaRepository<Guide, Long> {
    List<Guide> findByChampion(Champion champion);
}

