package com.nexushub.NexusHub.InGame.Rune;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexushub.NexusHub.InGame.Rune.domain.Rune;
import com.nexushub.NexusHub.InGame.Rune.domain.RunePath;
import com.nexushub.NexusHub.InGame.Rune.domain.RuneSlot;
import com.nexushub.NexusHub.InGame.Rune.repository.RunePathRepository;
import com.nexushub.NexusHub.InGame.Rune.repository.RuneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RuneService {
    @Value("${riot.patch-version}")
    private String patchVersion;

    private final RunePathRepository runePathRepository;
    private final RuneRepository runeRepository;

    public void importRunes() throws IOException {
        String url = "https://ddragon.leagueoflegends.com/cdn/"+patchVersion+"/data/ko_KR/runesReforged.json";
        ObjectMapper mapper = new ObjectMapper();

        JsonNode root = mapper.readTree(new URL(url));

        for (JsonNode pathNode : root) {
            RunePath path = new RunePath();
            path.setId(pathNode.get("id").asLong());
            path.setRuneKey(pathNode.get("key").asText());
            path.setName(pathNode.get("name").asText());
            path.setIcon(pathNode.get("icon").asText());

            List<RuneSlot> slots = new ArrayList<>();
            for (JsonNode slotNode : pathNode.get("slots")) {
                RuneSlot slot = new RuneSlot();
                slot.setRunePath(path);

                List<Rune> runes = new ArrayList<>();
                for (JsonNode runeNode : slotNode.get("runes")) {
                    Rune rune = new Rune();
                    rune.setId(runeNode.get("id").asLong());
                    rune.setRuneKey(runeNode.get("key").asText());
                    rune.setName(runeNode.get("name").asText());
                    rune.setIcon(runeNode.get("icon").asText());
                    rune.setShortDesc(runeNode.get("shortDesc").asText());
                    rune.setSlot(slot);
                    runes.add(rune);
                }

                slot.setRunes(runes);
                slots.add(slot);
            }

            path.setSlots(slots);
            runePathRepository.save(path);
        }
    }
}
