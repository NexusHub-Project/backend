package com.nexushub.NexusHub.Guide.controller;

// gpt 사용 목록
// 무슨 에러 catch 할지 물어봄
// import문, @RequiredArgsConstructor 무슨 뜻인지 물어봄
// 목업 데이터 어떻게 쓰냐고 물어봄 (그래서 domain 생성함)

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.nexushub.NexusHub.Guide.dto.GuideDto;
import java.util.List;
import java.util.stream.Collectors;
import com.nexushub.NexusHub.Guide.domain.Guide;
import com.nexushub.NexusHub.User.domain.User;
import java.time.LocalDateTime;

@RestController
@RequestMapping("api/strategy")
//@RequiredArgsConstructor
@Slf4j  //로그 출력
public class GuideController {

//    private final GuideService guideService;

    // 게시글 생성
    @PostMapping
    public GuideDto.GuideUploadResponseDto createGuide(@RequestBody GuideDto.Request request) {
        try {
            log.info("공략 create: title = {}", request.getTitle());

            Guide mockGuide = Guide.builder()
                    .id(1L)
                    .title(request.getTitle())
                    .content(request.getDescription())
                    .author(User.builder().id(1L).gameName("해찬오빠").build())
                    .views(0)
                    .likes(0)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            return new GuideDto.GuideUploadResponseDto(mockGuide);
        } catch (Exception e) {
            log.error("공략 create 실패: {}", e.getMessage());
            throw new RuntimeException("공략 create 실패: " + e.getMessage());
        }
    }

    // 전체 공략 조회
    @GetMapping
    public List<GuideDto.GuideListResponseDto> getGuideList() {
        try {
            List<Guide> mockList = List.of(
                    Guide.builder()
                            .id(1L)
                            .title("기본 공략")
                            .author(User.builder().id(1L).gameName("해찬오빠").build())
                            .views(100)
                            .createdAt(LocalDateTime.now().minusDays(2))
                            .build(),

                    Guide.builder()
                            .id(2L)
                            .title("스킬트리 정리")
                            .author(User.builder().id(2L).gameName("지혜누나").build())
                            .views(250)
                            .createdAt(LocalDateTime.now().minusDays(1))
                            .build()
            );

            return mockList.stream()
                    .map(GuideDto.GuideListResponseDto::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("공략 리스트 조회 실패: {}", e.getMessage());
            throw new RuntimeException("공략 list 실패: " + e.getMessage());
        }
    }

    // 단일 공략 조회
    @GetMapping("/{id}")
    public GuideDto.GuideResponseDto getGuideDetails(@PathVariable int id) {
        try {
            if (id < 1) {
                throw new IllegalArgumentException("id는 1 이상이어야 합니다.");
            }

            Guide mockGuide = Guide.builder()
                    .id((long) id)
                    .title("lorem ipsum")
                    .content("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.")
                    .author(User.builder().id(2L).gameName("태경오빠").build())
                    .views(123)
                    .likes(45)
                    .createdAt(LocalDateTime.now().minusDays(1))
                    .updatedAt(LocalDateTime.now())
                    .build();

            return new GuideDto.GuideResponseDto(mockGuide);
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

            Guide updatedGuide = Guide.builder()
                    .id((long) id)
                    .title(dto.getTitle())
                    .content(dto.getDescription())
                    .author(User.builder().id(1L).gameName("수정자").build())
                    .views(99)
                    .likes(10)
                    .createdAt(LocalDateTime.now().minusDays(1))
                    .updatedAt(LocalDateTime.now())
                    .build();

            return new GuideDto.GuideResponseDto(updatedGuide);
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

            // 실제로는 guideService.delete(id) 수행
            return ResponseEntity.ok("공략 삭제 완료: " + id);
        } catch (Exception e) {
            log.error("공략 삭제 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("공략 삭제 실패: " + e.getMessage());
        }
    }

}
