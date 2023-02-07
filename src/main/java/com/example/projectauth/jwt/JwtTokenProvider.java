package com.example.projectauth.jwt;

import com.example.projectauth.dto.TokenDto;
import com.example.projectauth.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000L*60*15; //15분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L*60*60*24*7; //7일


    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.refresh-key}")
    private String refreshKey;


    private final UserDetailsService userDetailsService;

    // 객체 초기화, secretKey 를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 토큰 생성
    public String createAccessToken(String userPk) {
        Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보단위
        Date now = new Date();
        Date accessTokenExpiresIn = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);
        return BEARER_TYPE + Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now)
                .setExpiration(accessTokenExpiresIn) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey) // 사용할 암호화 알고리즘과 signature 에 들어갈 secret 값 세팅
                .compact();
    }
    public TokenDto createToken(String userPk/*,List<String> roles*/) {

        Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보단위
//        claims.put("roles", roles);
        log.info("토큰을 만들때 authentication객체의 이름= "+userPk);
        Date now = new Date();
        Date accessTokenExpiresIn = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = BEARER_TYPE + Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now)
                .setExpiration(accessTokenExpiresIn) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey) // 사용할 암호화 알고리즘과 signature 에 들어갈 secret 값 세팅
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, refreshKey)
                .compact();

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    // JWT 토큰에서 인증 정보 조회
//    public Authentication getAuthentication(String token) {
//        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserPk(token));
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    private String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Request 의 Header 에서 token 값을 가져온다. "X-AUTH-TOKEN" : "TOKEN 값"
//    public String resolveToken(HttpServletRequest request) {
//        List<String> list = new ArrayList<>();
//        request.getHeaderNames().asIterator().forEachRemaining(list::add);
//        for (String a : list) {
//            log.info("리퀘스트에 담긴 헤더값 = " + a);
//        }
//        log.info("토큰 분석 진입");
//        return request.getHeader("Authorization");
//    }
    public String resolveToken(HttpServletRequest request) {
        String token = null;
        Cookie cookie = WebUtils.getCookie(request, "Authorization");
        if(cookie != null) token = cookie.getValue();
        return token;
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
