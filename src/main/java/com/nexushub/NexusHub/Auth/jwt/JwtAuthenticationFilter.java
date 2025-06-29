package com.nexushub.NexusHub.Auth.jwt;

import com.nexushub.NexusHub.User.domain.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7); // 7자리 끊고

            if (jwtUtil.isTokenValid(token)) {
                // 1) loginId, role 가져오기
                String loginId = jwtUtil.extractLoginId(token);
                Role role = jwtUtil.extractRole(token);

                // 2) 역할 정보를 Spring security에서 사용하는 GrantedAuthority 형식으로 만듦
                List<SimpleGrantedAuthority> authorities =
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+role.name()));

                // 3) 인증 객체를 생성할 때 위에서 만든 authorities 넣어주기
                Authentication authentication = new UsernamePasswordAuthenticationToken(loginId, null, authorities);

                // 4) 보안 컨텍스트에 새로운 인증 정보 설정하기
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
