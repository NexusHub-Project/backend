package com.nexushub.NexusHub.Statistics.controller;

import com.nexushub.NexusHub.InGame.Champion.ChampionService;
import com.nexushub.NexusHub.Statistics.domain.*;
import com.nexushub.NexusHub.Statistics.dto.TierResponseDto;
import com.nexushub.NexusHub.Statistics.service.TierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/statistics")
@Slf4j
@RequiredArgsConstructor
public class StatisticsController {
    private final TierService tierService;

    @GetMapping("/set/mock")
    public String setMock(){
        tierService.reset();
        tierService.save();
        return "success";
    }

    @GetMapping("/tier/top")
    public ResponseEntity<List<TierResponseDto>> topTiers() {
        List<TierTop> topTiers = tierService.getTopTiers();
        List<TierResponseDto> dtos = new ArrayList<>();

        for (TierTop top : topTiers) {
            dtos.add(new TierResponseDto(top) );
        }
        return ResponseEntity.ok(sort(dtos));
    }

    @GetMapping("/tier/jug")
    public ResponseEntity<List<TierResponseDto>> jugTiers() {
        List<TierJug> jugTiers = tierService.getJugTiers();
        List<TierResponseDto> dtos = new ArrayList<>();
        for (TierJug jug : jugTiers) {
            dtos.add(new TierResponseDto(jug));
        }
        return ResponseEntity.ok(sort(dtos));
    }

    @GetMapping("/tier/mid")
    public ResponseEntity<List<TierResponseDto>> midTiers() {
        List<TierMid> midTiers = tierService.getMidTiers();
        List<TierResponseDto> dtos = new ArrayList<>();
        for (TierMid mid : midTiers) {
            dtos.add(new TierResponseDto(mid));
        }
        return ResponseEntity.ok(sort(dtos));
    }

    @GetMapping("/tier/adc")
    public ResponseEntity<List<TierResponseDto>> adcTiers() {
        List<TierAdc> adcTiers = tierService.getAdcTiers();
        List<TierResponseDto> dtos = new ArrayList<>();
        for (TierAdc adc : adcTiers) {
            dtos.add(new TierResponseDto(adc));
        }
        return ResponseEntity.ok(sort(dtos));
    }
    @GetMapping("/tier/sup")
    public ResponseEntity<List<TierResponseDto>> supTiers() {
        List<TierSup> supTiers = tierService.getSupTiers();
        List<TierResponseDto> dtos = new ArrayList<>();
        for (TierSup sup : supTiers) {
            dtos.add(new TierResponseDto(sup));
        }
        return ResponseEntity.ok(sort(dtos));
    }

    private List<TierResponseDto> sort(List<TierResponseDto> dtos) {
        return dtos.stream().sorted(
                Comparator.comparing(TierResponseDto::getTier).thenComparing(Comparator.comparing(TierResponseDto::getScore).reversed()))
                .collect(Collectors.toList());
    }
}
