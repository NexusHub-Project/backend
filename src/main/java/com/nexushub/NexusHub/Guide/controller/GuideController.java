package com.nexushub.NexusHub.Guide.controller;


import com.nexushub.NexusHub.Exception.Normal.CannotFoundGuide;
import com.nexushub.NexusHub.Exception.Normal.CannotFoundUser;
import com.nexushub.NexusHub.Guide.service.GuideService;
import com.nexushub.NexusHub.User.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.nexushub.NexusHub.Guide.dto.GuideDto;

import java.util.ArrayList;
import java.util.List;
import com.nexushub.NexusHub.Guide.domain.Guide;
import com.nexushub.NexusHub.User.domain.User;

@Slf4j  //로그 출력
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/strategy")
public class GuideController {
    private final GuideService guideService;
    private final UserService userService;

    // 게시글 생성
    @PostMapping("/upload")
    public ResponseEntity<?> createGuide(
            @RequestBody GuideDto.Request request,
            @AuthenticationPrincipal String loginId)
    {
        try {
            log.info("공략 create: title = {}", request.getTitle());

            // 유저 정보 받아오기
            User author = userService.findByLoginId(loginId).orElseThrow(() -> new CannotFoundUser ("해당 유저 정보를 찾을 수 없습니다."));
            Guide saved = guideService.save(request, author);
            return ResponseEntity.ok(new GuideDto.GuideUploadResponseDto(saved));

        } catch (Exception e) {
            log.error("공략 create 실패: {}", e.getMessage());
            throw new RuntimeException("공략 create 실패: " + e.getMessage());
        }
    }

    // 전체 공략 조회
    @GetMapping("/list")
    public ResponseEntity<?> getGuideList() throws CannotFoundGuide {
        List<Guide> guideEntityList = guideService.findAll();

        if (guideEntityList.isEmpty()) {
            throw new CannotFoundGuide("공략이 존재하지 않습니다");
        }

        List<GuideDto.GuideListResponseDto> guideDtoList = new ArrayList<>();
        for (Guide guide : guideEntityList) {
            guideDtoList.add(new GuideDto.GuideListResponseDto(guide));
        }

        return ResponseEntity.ok(guideDtoList);
    }

    // 단일 공략 조회
    @GetMapping("/{id}")
    public GuideDto.GuideResponseDto getGuideDetails(@PathVariable int id) {
        try {

            return null;

        } catch (Exception e) {
            log.error("공략 get 실패: {}", e.getMessage());
            throw new RuntimeException("공략 get 실패: " + e.getMessage());
        }
    }

    // 공략 수정
    @PatchMapping("/{id}")
    public GuideDto.GuideResponseDto updateStrategy(@PathVariable int id,
                                                    @RequestBody GuideDto.Request dto) {
        try {
            log.info("id={} 공략 update 요청: title = {}", id, dto.getTitle());


            return null;
        } catch (Exception e) {
            log.error("공략 update 실패: {}", e.getMessage());
            throw new RuntimeException("공략 update 실패: " + e.getMessage());
        }
    }

    // 공략 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStrategy(@PathVariable int id) {
        try {
            log.info("id={} 공략 delete 요청", id);

            return null;
        } catch (Exception e) {
            log.error("공략 삭제 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("공략 삭제 실패: " + e.getMessage());
        }
    }

}
