package com.nexushub.NexusHub.Riot.Match.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchDto {

    private DataDto myData;

    @JsonProperty("metadata")
    private MetadataDto metadata;

    @JsonProperty("info")
    private InfoDto info;
}
