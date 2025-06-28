package com.nexushub.NexusHub.Match.dto.perks;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SelectionDto {
    private int perk; // 룬의 고유 ID
    private int var1;
    private int var2;
    private int var3;
}
