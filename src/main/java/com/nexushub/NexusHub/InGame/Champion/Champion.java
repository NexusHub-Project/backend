package com.nexushub.NexusHub.InGame.Champion;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Champion {
    @Id
    private Long id;

    private String nameEn;   // 영어 이름 (ex: Aatrox)
    private String nameKo;   // 한글 이름 (ex: 아트록스)
    private String imageUrl;

    private Integer hp;
    private Integer attack;
    private Integer defense;
    private Integer magic;

    @Builder
    public Champion(Long id, String nameEn, String nameKo, String imageUrl,
                    Integer hp, Integer attack, Integer defense, Integer magic) {
        this.id = id;
        this.nameEn = nameEn;
        this.nameKo = nameKo;
        this.imageUrl = imageUrl;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.magic = magic;
    }
}
