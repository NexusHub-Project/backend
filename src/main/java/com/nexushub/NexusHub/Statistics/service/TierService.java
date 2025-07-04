package com.nexushub.NexusHub.Statistics.service;

import com.nexushub.NexusHub.Exception.Normal.CannotFoundChampion;
import com.nexushub.NexusHub.InGame.Champion.Champion;
import com.nexushub.NexusHub.InGame.Champion.ChampionRepository;
import com.nexushub.NexusHub.Statistics.domain.*;
import com.nexushub.NexusHub.Statistics.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TierService {
    private final TierTopRepository tierTopRepository;
    private final TierAdcRepository tierAdcRepository;
    private final TierJugRepository tierJugRepository;
    private final TierMidRepository tierMidRepository;
    private final TierSupRepository tierSupRepository;
    private final ChampionRepository championRepository;

    private Champion findByChampionName(String championName) {
        return championRepository.findByNameKo(championName)
                .orElseThrow(() -> new CannotFoundChampion(championName + "라는 이름의 챔피언은 존재하지 않습니다"));
    }

    public void save(){
        this.saveTopChampion();
        this.saveAdcChampion();
        this.saveJugChampion();
        this.saveMidChampion();
        this.saveSupChampion();
    }
    public void reset(){
        tierAdcRepository.deleteAll();
        tierJugRepository.deleteAll();
        tierMidRepository.deleteAll();
        tierSupRepository.deleteAll();
        tierTopRepository.deleteAll();
    }

    private void saveTopChampion(){
        List<String> topChampions = List.of(
                "세트", "말파이트", "모데카이저", "리븐", "케일",
                "쉔", "아트록스", "이렐리아", "문도 박사", "잭스",
                "퀸", "우르곳", "피오라", "레넥톤", "워윅",
                "초가스", "케넨", "오른", "카밀", "다리우스",
                "갱플랭크", "그라가스", "나서스", "티모", "가렌",
                "블라디미르", "올라프", "사이온", "렝가", "요네",
                "그웬", "요릭", "야스오", "신지드", "하이머딩거",
                "제이스", "클레드", "트런들", "볼리베어", "자크",
                "나르", "크산테", "럼블", "라이즈", "일라오이",
                "사일러스", "베인", "카시오페아", "칼리스타", "탐 켄치",
                "판테온", "오로라", "갈리오", "아크샨", "뽀삐"
        );

        for (String championName : topChampions) {
            Champion champion = findByChampionName(championName);
            tierTopRepository.save(new TierTop(champion));
        }
    }
    private void saveJugChampion(){
        List<String> jungleChampions = List.of(
                "그라가스", "그레이브즈", "니달리", "녹턴", "누누와 윌럼프",
                "다이애나", "람머스", "렉사이", "렝가", "리 신", "릴리아",
                "마스터 이", "바이", "벨베스", "볼리베어", "브라이어", "브랜드",
                "비에고", "샤코", "세주아니", "쉬바나", "신 짜오", "아무무",
                "아이번", "에코", "엘리스", "오공", "올라프", "우디르",
                "워윅", "자르반 4세", "자크", "카서스", "카직스", "케인",
                "킨드레드", "탈론", "트런들", "피들스틱", "헤카림",
                "잭스", "모데카이저", "그웬", "스카너", "세트"
        );
        for (String championName : jungleChampions) {
            Champion champion = findByChampionName(championName);
            tierJugRepository.save(new TierJug(champion));
        }
    }
    private void saveMidChampion(){
        List<String> midChampions = List.of(
                "갈리오", "라이즈", "럭스", "르블랑", "리산드라", "말자하",
                "벡스", "벨코즈", "브랜드", "블라디미르", "사일러스", "세라핀",
                "스웨인", "신드라", "아리", "아우렐리온 솔", "아지르", "아칼리",
                "아크샨", "애니", "애니비아", "야스오", "에코", "오리아나",
                "요네", "조이", "제라스", "제드", "직스", "카르마",
                "카사딘", "카타리나", "코르키", "키아나", "탈론",
                "탈리야", "트위스티드 페이트", "판테온", "피즈", "흐웨이",
                "니코", "다이애나", "이렐리아", "빅토르", "제이스", "베이가",
                "오로라", "카시오페아", "말파이트", "세트", "트리스타나",
                "자이라", "럼블", "클레드", "케일"
        );
        for (String championName : midChampions) {
            Champion champion = findByChampionName(championName);
            tierMidRepository.save(new TierMid(champion));
        }
    }
    private void saveAdcChampion(){
        List<String> adcChampions = List.of(
                "드레이븐", "루시안", "미스 포츈", "바루스", "베인", "사미라",
                "세나", "스몰더", "시비르", "애쉬", "아펠리오스", "이즈리얼",
                "자야", "제리", "진", "징크스", "카이사", "칼리스타", "코그모",
                "트위치", "트리스타나", "닐라", "야스오"
        );
        for (String adcChampion : adcChampions) {
            Champion champion = findByChampionName(adcChampion);
            tierAdcRepository.save(new TierAdc(champion));
        }
    }
    private void saveSupChampion(){
        List<String> supportChampions = List.of(
                "노틸러스", "나미", "레오나", "레나타 글라스크", "렐", "룰루",
                "럭스", "라칸", "마오카이", "모르가나", "밀리오", "바드",
                "브라움", "브랜드", "블리츠크랭크", "세나", "세라핀", "소나",
                "소라카", "스웨인", "샤코", "쓰레쉬", "알리스타", "애쉬",
                "자이라", "잔나", "질리언", "카르마", "타릭", "탐 켄치",
                "파이크", "피들스틱", "하이머딩거", "벨코즈", "제라스",
                "오로라", "유미", "니코", "뽀삐"
        );
        for (String supportChampion : supportChampions) {
            Champion champion = findByChampionName(supportChampion);
            tierSupRepository.save(new TierSup(champion));
        }
    }

    public List<TierTop> getTopTiers(){
        return tierTopRepository.findAll();
    }
    public List<TierMid> getMidTiers(){
        return tierMidRepository.findAll();
    }
    public List<TierAdc> getAdcTiers(){
        return tierAdcRepository.findAll();
    }
    public List<TierSup> getSupTiers(){
        return tierSupRepository.findAll();
    }
    public List<TierJug> getJugTiers(){
        return tierJugRepository.findAll();
    }
}
