package com.example.jwtex.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            // 요청에서 토큰 가져오기
            String token = parseBearerToken(request);
            log.info("Filter is running.....................................");
            // 토큰 검사하기 JWT 이므로 인가 서버에 요청하지 않고도 검증 가능
            if (token != null && !token.equalsIgnoreCase("null")) {
                String userId = tokenProvider.validateAndGetUserId(token);
                log.info("Authenticated user ID : " + userId);
                // 인증완료
                // SecurityContextHolder에 등록해야 인증된 사용자라고 생각하므로 등록하기
                AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userId,// 인증된 사용자의 정보, 문자열이 아니여도 아무거나 넣을 수 있다..보통 UserDetails 주입
                        null,
                        AuthorityUtils.NO_AUTHORITIES
                );
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                // SecurityContext 생성
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                // authentication(입증) 인증 정보를 Context 에 넣기
                securityContext.setAuthentication(authenticationToken);
                // SecurityContextHolder 에 컨텍스트로 등록
                // => ThreadLocal 에 저장 ( 각 스레드별 하나의 컨텍스트 관리 )
                SecurityContextHolder.setContext(securityContext);
            }
        } catch(Exception e) {
            logger.error("Could not set user authentication in security context", e);
        }
        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
        // Http 요청의 헤더를 파싱해 Bearer토큰을 리턴
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7);

        return null;
    }
}
