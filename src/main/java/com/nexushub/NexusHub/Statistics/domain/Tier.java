package com.nexushub.NexusHub.Statistics.domain;

import com.nexushub.NexusHub.InGame.Champion.Champion;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

// 1) 이 클래스는 테이블 만들지 않고ㅗ 자식에게 필드만 물려줄 것을 표현
@MappedSuperclass
@Getter
public abstract class Tier { // abstract를 통해서 직접 객체를 못 만들도록 설정

    @JoinColumn(name="champion_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    protected Champion champion;

    // 챔피언의 티어
    @Column(name="tier")
    protected Integer tier;

    // 챔피언의 꿀챔 점수
    @Column(name="score")
    protected Integer score;

    @Column(name="pick_rate")
    protected Float pickRate;

    @Column(name="win_rate")
    protected Float winRate;

    @Column(name="score_diff")
    protected Integer scoreDiff;

    protected LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


    protected void setRandomValues() {
        this.tier = ThreadLocalRandom.current().nextInt(1, 6);
        this.score = ThreadLocalRandom.current().nextInt(40, 100);
        this.pickRate = (float) ThreadLocalRandom.current().nextDouble(0.1, 87.2);
        this.winRate = (float) ThreadLocalRandom.current().nextDouble(43.9, 60.0);
        this.scoreDiff = ThreadLocalRandom.current().nextInt(-20, 21);
    }
}
