package com.nexushub.NexusHub.Statistics.domain;

import com.nexushub.NexusHub.InGame.Champion.Champion;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.stat.Statistics;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TierMid extends Tier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder
    public TierMid(Champion champion) {
        this.champion = champion;

        //랜덤 값으로 나머지 필드 채우는 메소드 호출
        super.setRandomValues();
    }
}
