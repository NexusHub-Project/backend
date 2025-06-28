package com.nexushub.NexusHub.InGame.Rune.repository;

import com.nexushub.NexusHub.InGame.Rune.domain.Rune;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuneRepository extends JpaRepository<Rune, Long> {
}
