package com.nexushub.NexusHub.Riot.Data;

import com.nexushub.NexusHub.Riot.Data.Champion.Champion;
import com.nexushub.NexusHub.Riot.Data.Champion.ChampionService;
import com.nexushub.NexusHub.Riot.Data.Rune.RuneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChampionInitializer implements ApplicationRunner {
    private final ChampionService championService;
    private final RuneService runeService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // DB에 있는지 체크하기
        log.info("디비에 챔피언 정보 있는 지 체크하기");
        Optional<Champion> champion = championService.getChampionById(1L);
        if (champion.isPresent()) {
            log.info("디비에 챔피언 정보 있음 ㅅㄱ");
            return;
        }
        log.info("디비에 챔피언 정보 없으니까 추가 함;;");

        championService.importChampions();
        log.info("추가함 ㅅㄱ");
    }
}
