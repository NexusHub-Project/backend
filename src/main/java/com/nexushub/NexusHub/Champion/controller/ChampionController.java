package com.nexushub.NexusHub.Champion.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@Slf4j
@RequestMapping("/api/v1/championDetail")
public class ChampionController {

    @PostMapping("/{championName}")
    public ResponseEntity<String> getChampionDetails(@PathVariable("championName") String championName) {
        log.info("championName : {} ", championName);
         // 리턴 받아야 하는 것 : ChampionDetailDto를 받으면 돼

        return ResponseEntity.ok(championName);
    }
}