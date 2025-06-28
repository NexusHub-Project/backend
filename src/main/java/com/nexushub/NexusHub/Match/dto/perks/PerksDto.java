package com.nexushub.NexusHub.Match.dto.perks;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PerksDto {

    @JsonProperty("statPerks")
    private StatPerksDto statPerks;

    @JsonProperty("styles")
    private List<StyleDto> styles;
}