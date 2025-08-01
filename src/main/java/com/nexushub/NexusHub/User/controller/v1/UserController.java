package com.nexushub.NexusHub.User.controller.v1;

import com.nexushub.NexusHub.User.dto.request.UserCheckGameNameRequestDto;
import com.nexushub.NexusHub.User.dto.request.UserLoginRequestDto;
import com.nexushub.NexusHub.User.dto.request.UserSignUpRequestDto;
import com.nexushub.NexusHub.Exception.Fail.SignUpFail;
import com.nexushub.NexusHub.Exception.RiotAPI.CannotFoundSummoner;
import com.nexushub.NexusHub.Exception.RiotAPI.IsPresentLoginId;
import com.nexushub.NexusHub.Riot.dto.RiotAccountDto;
import com.nexushub.NexusHub.User.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserLoginRequestDto userLoginRequestDto) throws ChangeSetPersister.NotFoundException {
        // 로그인 하기
        log.info("Login request: {}", userLoginRequestDto);

        Map<String, Object> response = userService.login(userLoginRequestDto);


        if (response.get("login").toString().equals("true")){
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    @PostMapping("/check/nickName")
    public ResponseEntity<Map<String, Boolean>> nickNameCheck(@RequestBody UserSignUpRequestDto.NickNameRequestDto dto){
        boolean b = userService.checkPossibleUsingNickName(dto.getNickName());

        Map<String, Boolean> response = new HashMap<>();
        response.put("isPresentNickName", b);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/check/loginId")
    public ResponseEntity<Map<String, Boolean>> loginIdCheck(@RequestBody UserSignUpRequestDto.LoginIdRequestDto dto){
        // 로그인 아이디 중복 체크
        log.info("LoginIdCheck request received");
        log.info("loginId:{}", dto.getLoginId());

        boolean isPresent = userService.loginIdCheckV1(dto.getLoginId());
//        boolean isPresent = userService.loginIdCheckV2(dto.getLoginId());

        Map<String, Boolean> response = new HashMap<>();
        response.put("isPresentId", isPresent);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/check/gameName")
    public ResponseEntity<Map<String, Object>> gameNameCheck(@RequestBody UserCheckGameNameRequestDto dto) throws CannotFoundSummoner {
        // 소환사 이름 존재 여부 체크 -> 라이엇 API에 요청을 해서 존재 여부를 파악 필요 함

        log.info("GameNameCheck request received");
        log.info("GameName:{} ", dto);

        Map<String, Object> response = new HashMap<>();

        RiotAccountDto isPresentGameName = userService.gameNameCheck(dto.getGameName(), dto.getTagLine());

        if (isPresentGameName != null){
            response.put("isPresentNickName", "true");
            response.put("puuid", isPresentGameName.getPuuid());
            response.put("gameName", dto.getGameName());
            response.put("tagLine", dto.getTagLine());
        }
        else response.put("isPresentGameName", "false");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup (@RequestBody UserSignUpRequestDto dto) throws IsPresentLoginId {

        if (dto.getIsPresentGameName() == null || dto.getIsPresentId() == null || dto.getIsPresentNickName() == null){
            throw new SignUpFail("아이디 중복 확인, 소환사명 인증 혹은 닉네임 중복 확인을 먼저 진행 해주세요.");
        }


        log.info("Signup request: {}", dto);
        Boolean enrollResult = userService.enrollV2(dto);

        Map<String, Object> response = new HashMap<>();
        if (enrollResult){
            response.put("enroll", "success");
        }
        else {
            response.put("enroll", "fail");
        }
        return ResponseEntity.ok(response);
    }
}
