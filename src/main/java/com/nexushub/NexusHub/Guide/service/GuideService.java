package com.nexushub.NexusHub.Guide.service;

import com.nexushub.NexusHub.Exception.Fail.DeleteFail;
import com.nexushub.NexusHub.Exception.Fail.EditFail;
import com.nexushub.NexusHub.Exception.Normal.CannotFoundGuide;
import com.nexushub.NexusHub.Guide.domain.Guide;
import com.nexushub.NexusHub.Guide.dto.GuideDto;
import com.nexushub.NexusHub.Guide.repository.GuideRepository;
import com.nexushub.NexusHub.InGame.Champion.Champion;
import com.nexushub.NexusHub.User.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GuideService {

    private final GuideRepository guideRepository;

    // 공략 저장
    public Guide save(GuideDto.Request dto, User author, Champion champion) {
        log.info("title : {}, content : {}, author : {}, champion : {}", dto.getTitle(), dto.getContent(), author.getGameName(), champion.getNameKo());
        Guide guide = new Guide(
                dto.getTitle(),
                dto.getContent(),
                champion,
                author
        );

        return guideRepository.save(guide);
//        return guide;
    }

    // 전체 조회
    public List<Guide> findAll() {
        return guideRepository.findAll();
    }

    // 단일 조회
    // 수정 사항 *) view를 여기서 호출하지 않기로 함
    public Optional<Guide> findById(Long id) throws CannotFoundGuide {
//        Guide guide = guideRepository.findById(id)
//                .orElseThrow(() -> new CannotFoundGuide("해당 공략이 존재하지 않습니다."));
//        guide.view();
//        return guide;

        return guideRepository.findById(id);
    }
    public List<Guide> findByChampion(Champion champion) {
        return guideRepository.findByChampion(champion);
    }

    // 수정
    // 수정 사항 4) edit 메소드 안에서 해당 유저가 작성한 게 맞는지 판별 후에 수정 하기
    public Guide edit(Long id, GuideDto.Request dto, User author) throws CannotFoundGuide {
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new CannotFoundGuide("해당 공략이 존재하지 않습니다."));

        if (isAuthor(guide, author)) {
            guide.update(dto.getTitle(), dto.getContent());
        }
        else {
            throw new EditFail("작성자만 수정 가능합니다.");
        }

        return guide;
    }

    // 삭제
    // 수정 사항 6) delete 메소드 안에서 해당 유저가 작성한 게 맞는지 판별 후에 삭제 하기
    public void deleteById(Long id, User author) throws CannotFoundGuide {
        Guide guide = findById(id)
                .orElseThrow(()-> new CannotFoundGuide("해당 공략 정보를 찾을 수 없습니다."));

        if (isAuthor(guide, author)) {
            guideRepository.deleteById(id);
        }
        else {
            throw new DeleteFail("작성자만 삭제 가능합니다.");
        }

    }

    // 좋아요
    public void addLikeById(Long id) {
        guideRepository.findById(id).ifPresent(Guide::like);
    }

    //싫어요
    public void addDislikeById(Long id) { guideRepository.findById(id).ifPresent(Guide::dislike); }

    // 조회수 증가
    // 수정 사항 7) 찾을 때 이 메소드 호출해서 조회수 올리기 -> 수정 전에는 해당 메소드가 사용되지 않았음 -> 그냥 객체의 view() 메소드 이용하면 됨 JPA로 해당 객체의 값을 변경하면 알아서 반영됨
    public void addViews(Guide guide){
        guide.view();
    }

    private boolean isAuthor(Guide guide, User author) {
        return guide.getAuthor().equals(author);
    }
}