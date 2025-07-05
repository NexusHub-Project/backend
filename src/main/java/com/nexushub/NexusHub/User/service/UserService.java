package com.nexushub.NexusHub.User.service;

import com.nexushub.NexusHub.User.dto.request.UserLoginRequestDto;
import com.nexushub.NexusHub.User.dto.request.UserSignUpRequestDto;
import com.nexushub.NexusHub.Auth.jwt.JwtUtil;
import com.nexushub.NexusHub.Exception.Fail.CannotSignUp;
import com.nexushub.NexusHub.Exception.RiotAPI.CannotFoundSummoner;
import com.nexushub.NexusHub.Exception.RiotAPI.IsPresentLoginId;
import com.nexushub.NexusHub.Riot.dto.RiotAccountDto;
import com.nexushub.NexusHub.Riot.service.RiotApiService;
import com.nexushub.NexusHub.User.domain.User;
import com.nexushub.NexusHub.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final RiotApiService riotApiService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Boolean enrollV1(UserSignUpRequestDto dto){
        User user = User.of(dto, passwordEncoder);
        User save = userRepository.save(user);
        return save!=null;
    }
    public Boolean enrollV2(UserSignUpRequestDto dto) throws IsPresentLoginId {
        // 사전 체크
        if (!checkPossibleByGameNameAndLoginId(dto)) {
            throw new CannotSignUp("아이디 중복 확인 또는 소환사명 인증을 먼저 진행해주세요.");
        }

        // 아이디 중복 예외를 직접 던짐
        if (loginIdCheckV1(dto.getLoginId())) {
            throw new IsPresentLoginId("이미 사용 중인 아이디입니다.");
        }

        // loginId가 null인 경우를 방지하기 위해 추가
        if (dto.getLoginId() == null || dto.getLoginId().isBlank()) {
            throw new CannotSignUp("필수 입력 값이 누락 되었습니다.");
        }

        User user = User.of(dto, passwordEncoder);
        userRepository.save(user);

        return true;
    }

    public Map<String, Object> login(UserLoginRequestDto dto) {
        Optional<User> user0 = userRepository.findByLoginId(dto.getLoginId());
        Map<String, Object> response = new HashMap<>();

        if (!user0.isPresent()) {
            response.put("status", HttpStatus.UNAUTHORIZED.value());
            response.put("message", "Invalid Id");
            response.put("login", false);
            return response;
        }


        User user = user0.get();


        if (user == null | !passwordEncoder.matches(dto.getLoginPw(), user.getLoginPw())){
            response.put("status", HttpStatus.UNAUTHORIZED.value());
            response.put("message", "Incorrect login or password");
            response.put("login", false);
            return response;
        }

        // Role로 전달하도록 수정
        String token = jwtUtil.createToken(user.getLoginId(), user.getRole());
        response.put("user_id", user.getId());
        response.put("token", token);
        response.put("message", "Login Success");
        response.put("login", true);
        response.put("nickName", user.getNickName());
        return response;
    }

    public Boolean loginIdCheckV1(String loginId){
        log.info("loginIdCheck [V1]: loginId={}", loginId);
        return userRepository.findByLoginId(loginId).isPresent();
    }

    public Boolean loginIdCheckV2(String loginId){
        log.info("loginIdCheck [V2]: loginId={}", loginId);
        boolean present = userRepository.findByLoginId(loginId).isPresent();
        try{
            if (present == false){
                return false;
            }
            else throw new IsPresentLoginId("loginId은 중복된 아이디 입니다. ");
        } catch (IsPresentLoginId e){
            return true;
        }
    }

    public RiotAccountDto gameNameCheck(String gameName, String tagLine) throws CannotFoundSummoner {
        return riotApiService.getSummonerInfo(gameName, tagLine);
    }

    public Optional<User> findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId);
    }

    private boolean checkPossibleByGameNameAndLoginId(UserSignUpRequestDto dto){
        return dto.getIsPresentGameName() == true && dto.getIsPresentId() == false && dto.getIsPresentNickName() == false;
    }

    public boolean checkPossibleUsingNickName(String nickName) {
        return userRepository.findByNickName(nickName).isPresent();
    }
}
