package com.nexushub.NexusHub.Riot.AdditionalData.controller;

import com.nexushub.NexusHub.Common.Exception.RiotAPI.CannotFoundSummoner;
import com.nexushub.NexusHub.Riot.AdditionalData.service.AdditionalDataService;
import com.nexushub.NexusHub.Riot.Ranker.domain.Tier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/additionalData")
@Slf4j
@RequiredArgsConstructor
public class AdditionalDataController {

    private final AdditionalDataService additionalService;

    @GetMapping("/iconAndLevel")
    public String divisionRequest() throws CannotFoundSummoner, InterruptedException {
        additionalService.downloadRankersProfile();
        return "good";
    }
}
