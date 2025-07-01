package com.nexushub.NexusHub.Guide.service;

import com.nexushub.NexusHub.Exception.Normal.CannotFoundGuide;
import com.nexushub.NexusHub.Guide.domain.Guide;
import com.nexushub.NexusHub.Guide.dto.GuideDto;
import com.nexushub.NexusHub.Guide.repository.GuideRepository;
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
    public Guide save(GuideDto.Request dto, User author) {
        Guide guide = Guide.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .author(author)
                .build();
        return guideRepository.save(guide);
    }

    // 전체 조회
    public List<Guide> findAll() {
        return guideRepository.findAll();
    }

    // 단일 조회
    public Guide findById(Long id) throws CannotFoundGuide {
        return guideRepository.findById(id)
                .orElseThrow(() -> new CannotFoundGuide("해당 공략이 존재하지 않습니다."));
    }

    // 수정
    public Guide edit(Long id, GuideDto.Request dto) throws CannotFoundGuide {
        Guide guide = findById(id);
        guide.update(dto.getTitle(), dto.getContent());
        return guide;
    }

    // 삭제
    public void deleteById(Long id) {
        guideRepository.deleteById(id);
    }

    // 좋아요
    public void addLike(Long id) {
        guideRepository.findById(id).ifPresent(Guide::like);
    }

    // 조회수 증가
    public void addView(Long id) {
        guideRepository.findById(id).ifPresent(Guide::view);
    }
}