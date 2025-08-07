package com.nexushub.NexusHub.Riot.Data;


import com.nexushub.NexusHub.Riot.Data.Champion.ChampionService;
import com.nexushub.NexusHub.Riot.Data.Rune.RuneService;
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
}
