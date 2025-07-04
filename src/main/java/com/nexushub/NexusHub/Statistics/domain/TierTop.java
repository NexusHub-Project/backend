package com.nexushub.NexusHub.Statistics.domain;

import com.nexushub.NexusHub.InGame.Champion.Champion;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TierTop extends Tier{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder
    public TierTop(Champion champion) {
        this.champion = champion;
        super.setRandomValues();
    }
}
