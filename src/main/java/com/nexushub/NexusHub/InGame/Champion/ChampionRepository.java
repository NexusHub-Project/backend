package com.nexushub.NexusHub.InGame.Champion;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChampionRepository extends JpaRepository<Champion, Long> {
    Optional<Champion> findByNameKo(String championName);
    Optional<Champion> findByNameEn(String championName);
}
