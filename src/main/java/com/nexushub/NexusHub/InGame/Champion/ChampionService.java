package com.nexushub.NexusHub.InGame.Champion;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexushub.NexusHub.Exception.Normal.CannotFoundChampion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.OptionalInt;

@Service("inGameChampionService")
@RequiredArgsConstructor
@Slf4j
public class ChampionService {
    @Value("${riot.patch-version}")
    private String patchVersion;

    private final ChampionRepository championRepository;
    private final ObjectMapper objectMapper;

    public void importChampions() throws IOException {
        String url = "https://ddragon.leagueoflegends.com/cdn/"+patchVersion+"/data/ko_KR/champion.json";
        JsonNode root = objectMapper.readTree(new URL(url));
        JsonNode dataNode = root.get("data");

        for (JsonNode champNode : dataNode) {
            log.info("Champion found: " + champNode.get("name").asText());
            String nameEn = champNode.get("id").asText();   // Aatrox
            String nameKo = champNode.get("name").asText(); // 아트록스
            String imageFull = champNode.get("image").get("full").asText(); // Aatrox.png
            Long id = champNode.get("key").asLong(); // key 값은 Long

            Integer hp = champNode.get("stats").get("hp").asInt();
            Integer attack = champNode.get("info").get("attack").asInt();
            Integer defense = champNode.get("info").get("defense").asInt();
            Integer magic = champNode.get("info").get("magic").asInt();

            String imageUrl = "https://ddragon.leagueoflegends.com/cdn/14.11.1/img/champion/" + imageFull;

            Champion champion = Champion.builder()
                    .id(id)
                    .nameEn(nameEn)
                    .nameKo(nameKo)
                    .imageUrl(imageUrl)
                    .hp(hp)
                    .attack(attack)
                    .defense(defense)
                    .magic(magic)
                    .build();

            championRepository.save(champion);
        }
    }
    public Optional<Champion> getChampionById(Long id) {
        return championRepository.findById(id);
    }
}
