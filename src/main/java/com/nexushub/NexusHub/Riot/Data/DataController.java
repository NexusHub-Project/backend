package com.nexushub.NexusHub.Riot.Data;


import com.nexushub.NexusHub.Common.Exception.RiotAPI.CannotFoundSummoner;
import com.nexushub.NexusHub.Riot.AdditionalData.service.AdditionalDataService;
import com.nexushub.NexusHub.Riot.Data.Champion.ChampionService;
import com.nexushub.NexusHub.Riot.Data.Rune.RuneService;
import com.nexushub.NexusHub.Riot.Data.SummonerSpell.SummonerSpellService;
import com.nexushub.NexusHub.Riot.Ranker.Sheduler.RankerScheduler;
import com.nexushub.NexusHub.Riot.Ranker.service.RankerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor
@RequestMapping("/import")
public class DataController {
    private final ChampionService championService;
    private final RuneService runeService;
    private final SummonerSpellService summonerSpellService;
    private final AdditionalDataService additionalService;
    private final RankerService rankerService;
    private final RankerScheduler rankerScheduler;

    @GetMapping("/champion")
    public ResponseEntity<String> importChampions() {
        try {
            championService.importChampions();
            return ResponseEntity.ok("챔피언 데이터 저장 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("오류 발생: " + e.getMessage());
        }
    }


    @GetMapping("/rune")
    public ResponseEntity<String> importRunes() {
        try {
            runeService.importRunes();
            return ResponseEntity.ok("룬 데이터 저장 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("룬 데이터 저장 실패: " + e.getMessage());
        }
    }

    @GetMapping("/spell")
    public ResponseEntity<String> importSpells(){
        try{
            summonerSpellService.importSummonerSpells();
            return ResponseEntity.ok("소환사 주문 데이터 저장 완료");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("소환사 주문 데이터 저장 실패: " + e.getMessage());
        }
    }

    @GetMapping("/iconAndLevel")
    public String divisionRequest() throws CannotFoundSummoner, InterruptedException {
        additionalService.downloadRankersProfile();
        return "good";
    }

    @GetMapping("/store-rankers/challenger")
    public ResponseEntity<String> storeRankers() throws InterruptedException {
        // 단순 디비에 저장하는 용도
        rankerService.saveChallenger();

        return ResponseEntity.ok("저장 완료");
    }
    @GetMapping("/store-rankers/grandmaster")
    public ResponseEntity<String> storeRankersG() throws InterruptedException {
        // 단순 디비에 저장하는 용도
        rankerService.saveGrandMasters();

        return ResponseEntity.ok("저장 완료");
    }
    @GetMapping("/store-rankers/master")
    public ResponseEntity<String> storeRankersM() throws InterruptedException {
        // 단순 디비에 저장하는 용도
        rankerService.saveMasters();

        return ResponseEntity.ok("저장 완료");
    }
    @Operation(summary = "랭킹을 직접 업데이트", description = "PUUID를 통해 직접 랭킹을 업데이트 함")
    @GetMapping("/update-ranker/directly")
    public String updateRakingDirect(){
        rankerScheduler.scheduleRankingUpdate();
        return "good";
    }
}
