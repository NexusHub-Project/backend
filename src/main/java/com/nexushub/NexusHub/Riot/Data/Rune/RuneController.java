package com.nexushub.NexusHub.Riot.Data.Rune;

import com.nexushub.NexusHub.Riot.Data.Champion.ChampionService;
import com.nexushub.NexusHub.Riot.Data.Champion.dto.ChampionReturnDto;
import com.nexushub.NexusHub.Riot.Data.Rune.domain.Rune;
import com.nexushub.NexusHub.Riot.Data.Rune.dto.RuneResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rune")
public class RuneController  {

    private final RuneService runeService;

    @GetMapping("/{id}")
    public RuneResponseDto getChampInfo(@PathVariable Long id){
        return runeService.getRuneInfoById(id);
    }

}