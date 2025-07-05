package com.nexushub.NexusHub.User.domain;


import com.nexushub.NexusHub.Auth.dto.request.UserSignUpRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Table(name="users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name="login_id", length = 50, nullable = false, unique = true)
    private String loginId;

    @Column(name="login_pw", length = 255, nullable = false )
    private String loginPw;

    @Column(name="game_name", length = 100)
    private String gameName;

    @Column(name="nick_name",length = 20)
    private String nickName;

    @Column(name="tag_line", length = 20)
    private String tagLine;

    @Column(name = "puuid", length = 100)
    private String puuid;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static User of (UserSignUpRequestDto dto, PasswordEncoder encoder) {
        return User.builder()
                .loginId(dto.getLoginId())
                .loginPw(encoder.encode(dto.getLoginPw()))
                .gameName(dto.getGameName())
                .puuid(dto.getPuuid())
                .tagLine(dto.getTagLine())
                .role(Role.USER) // 기본 역할을 USER로 만들기
                .build();
    }
}
