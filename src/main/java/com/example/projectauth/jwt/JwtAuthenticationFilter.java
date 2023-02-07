package com.example.projectauth.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;


@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 헤더에서 JWT 를 받아온다.
        log.info("jwtauthenticationFilter 진입");
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        log.info("리퀘스트에 담긴 토큰 = "+ token);
        // 저장된 JWT 에서 "Bearer " 문자열 부분 제거하고 토큰값만 남김
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            token = token.substring(6);
        }
        // 유효한 토큰인지 확인한다.
        if (token != null && jwtTokenProvider.validateToken(token)) {
            log.info("토큰 유효성 검사 시작");
            try {
                // 토큰이 유효하면 토큰으로부터 유저 정보를 받아온다.
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                // SecurityContext 에 Authentication 객체를 저장한다.
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Authentication 이름 ="+ authentication.getName());
            } catch (IllegalArgumentException e) {
                logger.error("an error occured during getting username from token", e);
                throw new JwtException("유효하지 않은 토큰");
            } catch (ExpiredJwtException e) {
                logger.warn("the token is expired and not valid anymore", e);
                throw new JwtException("토큰 기한 만료");
            } catch (SignatureException e) {
                logger.error("Authentication Failed. Username or Password not valid.");
                throw new JwtException("사용자 인증 실패");
            }
        }
        chain.doFilter(request, response);
    }

}