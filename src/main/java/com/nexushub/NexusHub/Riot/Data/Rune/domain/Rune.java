package com.nexushub.NexusHub.Riot.Data.Rune.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString(exclude = "slot") // 순환 참조를 막기 위해 slot 필드를 제외
public class Rune {

    @Id
    private Long id;

    @Column(name = "rune_key")
    private String runeKey;

    private String name;
    private String icon;

    @Column(name = "short_desc", columnDefinition = "LONGTEXT")
    private String shortDesc;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    private RuneSlot slot;
}