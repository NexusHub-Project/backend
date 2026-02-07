package com.nexushub.NexusHub.Score.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nexushub.NexusHub.Riot.Match.dto.InfoDto;
import com.nexushub.NexusHub.Riot.Match.dto.MetadataDto;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchModelDto {
    @JsonProperty("metadata")
    private MetadataDto metadata;

    @JsonProperty("info")
    private InfoModelDto info;
}
