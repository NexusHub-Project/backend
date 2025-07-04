package com.nexushub.NexusHub.Auth;

import com.nexushub.NexusHub.Auth.jwt.JwtAuthenticationFilter;
import com.nexushub.NexusHub.Exception.Handler.PatchNoteAccessDenieHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private final PatchNoteAccessDenieHandler patchNoteAccessDenieHandler;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/user/**",
                                "/api/v1/summoner/**",
                                "/import/**",
                                "/api/v1/patchnote/test", "/api/v1/patchnote/find/*", "/api/v1/patchnote/edit/*", "/api/v1/patchnote/delete/*", "/api/v1/patchnote/show/**",
                                "/api/v1/comment/**",
                                "/api/v1/strategy/**",
                                "/api/v1/statistics/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .anyRequest().authenticated()
//
                )
                .exceptionHandling(exception -> exception.accessDeniedHandler(patchNoteAccessDenieHandler)) // patchnote 접근 권한 오류 핸들러
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
