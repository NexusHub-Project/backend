package com.nexushub.NexusHub.Riot.Data.Champion.repository;

import com.nexushub.NexusHub.Riot.Data.Champion.Champion;
import com.nexushub.NexusHub.Riot.Data.Champion.ChampionSpell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ChampionSpellRepository extends JpaRepository<ChampionSpell, Long> {
    List<ChampionSpell> getSpellsFindByChampion(Champion champion);

}