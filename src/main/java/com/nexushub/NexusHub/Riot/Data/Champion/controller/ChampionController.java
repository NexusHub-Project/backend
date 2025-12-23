package com.nexushub.NexusHub.Riot.Data.Champion.controller;


import com.nexushub.NexusHub.Common.Exception.Normal.CannotFoundChampion;
import com.nexushub.NexusHub.Riot.Data.Champion.Champion;
import com.nexushub.NexusHub.Riot.Data.Champion.ChampionService;
import com.nexushub.NexusHub.Riot.Data.Champion.dto.ChampionReturnDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/champion")
public class ChampionController {

    private final ChampionService championService;

    @GetMapping("/{id}")
    public ChampionReturnDto getChampInfo(@PathVariable Long id){
        return championService.getChampionInfoDto(id);
    }
}
