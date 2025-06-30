package com.nexushub.NexusHub.Guide.controller;

// 무슨 에러 catch 할지 물어봄
// import문, @RequiredArgsConstructor 무슨 뜻인지 물어봄

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/strategy")
//@RequiredArgsConstructor
@Slf4j  //로그 출력
public class GuideController {

//    private final GuideService guideService;

    // 게시글 생성
    @PostMapping
    public String createStrategy() {
        try {
            log.info("공략 create");
            // 실제 로직 들어갈 자리
            return "공략 create 완료";
        } catch (Exception e) {
            log.error("공략 생성 중 에러 발생: {}", e.getMessage());
            return "공략 create 실패: " + e.getMessage();
        }
    }

    // 전체 공략 조회
    @GetMapping
    public String getStrategy() {
        try {
            log.info("전체 공략 get");
            return "전체 공략 get 완료";
        } catch (Exception e) {
            log.error("전체 공략 조회 중 에러 발생: {}", e.getMessage());
            return "전체 공략 get 실패: " + e.getMessage();
        }
    }

    // 단일 공략 조회
    @GetMapping("/{id}")
    public String getStrategyDetails(@PathVariable int id) {
        try {
            log.info("id={} 공략 get", id);

            if (id < 1) {
                throw new IllegalArgumentException("id는 1 이상");
            }

            return "공략 get 완료: " + id;
        } catch (IllegalArgumentException e) {
            log.warn("잘못된 요청: {}", e.getMessage());
            return "공략 get 실패: " + e.getMessage();
        } catch (Exception e) {
            log.error("공략 조회 중 에러 발생: {}", e.getMessage());
            return "공략 get 실패: " + e.getMessage();
        }
    }

    // 공략 수정
    @PatchMapping("/{id}")
    public String updateStrategy(@PathVariable int id) {
        try {
            log.info("id={} 공략 update", id);
            return "공략 update 완료: " + id;
        } catch (Exception e) {
            log.error("공략 수정 중 에러 발생: {}", e.getMessage());
            return "공략 update 실패: " + e.getMessage();
        }
    }

    // 공략 삭제
    @DeleteMapping("/{id}")
    public String deleteStrategy(@PathVariable int id) {
        try {
            log.info("id={} 공략 delete", id);
            return "공략 delete 완료: " + id;
        } catch (Exception e) {
            log.error("공략 삭제 중 에러 발생: {}", e.getMessage());
            return "공략 delete 실패: " + e.getMessage();
        }
    }

}
