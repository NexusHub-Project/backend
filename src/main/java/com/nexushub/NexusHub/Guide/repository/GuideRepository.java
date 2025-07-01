package com.nexushub.NexusHub.Guide.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nexushub.NexusHub.Guide.domain.Guide;

public interface GuideRepository extends JpaRepository<Guide, Long> {

}

