package com.nexushub.NexusHub.Statistics.controller;

import com.nexushub.NexusHub.Statistics.domain.Champion.ChampionStatsByPosition;
import com.nexushub.NexusHub.Statistics.dto.ChampionDetailDto;
import com.nexushub.NexusHub.Statistics.dto.ChampionNameResDto;
import com.nexushub.NexusHub.Statistics.dto.TierResponseDto;
import com.nexushub.NexusHub.Statistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@RestController
@RequestMapping("/api/v1/statistics")
@Slf4j
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/set/mock")
    public String setMock(){
        statisticsService.createAllPositionStatistics();
        return "success";
    }


    @GetMapping("/tier/{position}")
    public ResponseEntity<List<TierResponseDto>> getPositionTier(@PathVariable String position){
        List<ChampionStatsByPosition> tiers = statisticsService.getTiers(position);
        List<TierResponseDto> dtos = new ArrayList<>();

        for (ChampionStatsByPosition tier : tiers) {
            dtos.add(new TierResponseDto(tier));
        }
        dtos.sort((t1,t2)->t1.getTier() - t2.getTier());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/championDetail/{championName}")
    public ResponseEntity<List<ChampionDetailDto>> getChampionDetail(@PathVariable String championName){
        List<ChampionDetailDto> dtos = statisticsService.getChampionDetail(championName);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/champions/all")
    public ResponseEntity<Queue<ChampionNameResDto>> getAllChampionsName() {
        Queue<ChampionNameResDto> dtos = statisticsService.getAllName();
        return ResponseEntity.ok(dtos);
    }

}
