package com.nexushub.NexusHub.Riot.Data.SummonerSpell.controller;

import com.nexushub.NexusHub.Riot.Data.SummonerSpell.SmmrSpell;
import com.nexushub.NexusHub.Riot.Data.SummonerSpell.SummonerSpellService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/summoner-spell")
public class SummonerSpellController {
    private final SummonerSpellService summonerSpellService;

    @GetMapping("{id}")
    public ResponseEntity<SmmrSpell> getSspell(Long id){
        return ResponseEntity.ok(summonerSpellService.getSmmrSpellById(id));
    }
}
