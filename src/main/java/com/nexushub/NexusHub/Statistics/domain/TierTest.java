package com.nexushub.NexusHub.Statistics.domain;

import com.nexushub.NexusHub.InGame.Champion.Champion;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.NoArgsConstructor;

//@Entity
@NoArgsConstructor
public class TierTest extends Tier{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    public TierTest(Champion champion) {
        this.champion = champion;
    }
}
