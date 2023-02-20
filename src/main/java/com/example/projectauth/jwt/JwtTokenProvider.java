package com.example.projectauth.jwt;

import com.example.projectauth.dto.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {
    private static final String BEARER_TYPE = "Bearer ";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000L*60*2; //5분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L*60*60*3; //3시간


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

    /*
    JWT Access 토큰 , Refresh 토큰 생성
     */
    public TokenDto createToken(String userPk/*,List<String> roles*/) {
//        claims.put("roles", roles);
        Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보단위
        Date now = new Date();
        Date accessTokenExpiresIn = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);
        log.info("만료시간= " +accessTokenExpiresIn.getTime());
        log.info("현재시간= " +now.getTime());

        // Access Token 생성
        String accessToken = Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now)
                .setExpiration(accessTokenExpiresIn) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey) // 사용할 암호화 알고리즘과 signature 에 들어갈 secret 값 세팅
                .compact();
        log.info("토큰을 만들때 authentication객체의 이름= "+userPk);

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
    public Authentication getAuthentication(String token) {
        log.info("authentication을 가져올때 들어가는 이름 ="+this.getUserPk(token));
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        log.info("유저 pk(이름)="+Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject());
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        log.info("ResolveToken 쿠키에 담긴 토큰 ="+ token);
        return token;
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            log.info("accesss token claims정보"+ claims);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
    public boolean validateRefreshToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(refreshKey).parseClaimsJws(jwtToken);
            log.info("refresh token claims정보"+ claims);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
