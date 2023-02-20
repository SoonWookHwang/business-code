package com.example.projectauth.service;

import com.example.projectauth.dto.LoginRequestDto;
import com.example.projectauth.dto.LoginResponseDto;
import com.example.projectauth.dto.SignupRequestDto;
import com.example.projectauth.dto.TokenDto;
import com.example.projectauth.error.exception.EntityNotFoundException;
import com.example.projectauth.error.exception.ErrorCode;
import com.example.projectauth.error.exception.InvalidValueException;
import com.example.projectauth.jwt.JwtTokenProvider;
import com.example.projectauth.model.Member;
import com.example.projectauth.model.RefreshToken;
import com.example.projectauth.repository.MemberRepository;
import com.example.projectauth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;



    public void registerUser(SignupRequestDto requestDto/*, MultipartFile filePath*/) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
//        String img = requestDto.getImg();
        String pattern = "^[a-zA-Z0-9]*$";


        //아이디 중복 처리
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new InvalidValueException(ErrorCode.USERNAME_DUPLICATION);
        }

        //회원가입 요구형식 처리
        if (username.length() < 4) {
            throw new InvalidValueException(ErrorCode.INVALID_INPUT_USERNAME);
        } else if (!Pattern.matches(pattern, username)) {
            throw new InvalidValueException(ErrorCode.INVALID_USERNAME);
//        } else if (!password.equals(passwordCheck)) {
//            throw new InvalidValueException(ErrorCode.NOTEQUAL_INPUT_PASSWORD);
        } else if (password.length() < 4) {
            throw new InvalidValueException(ErrorCode.INVALID_PASSWORD);
        }
        userRepository.save(new Member(username, passwordEncoder.encode(password)));
    }

    public TokenDto login(LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        Member member = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOTFOUND_USER));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
            throw new InvalidValueException(ErrorCode.LOGIN_INPUT_INVALID);
        }
        TokenDto tokens = jwtTokenProvider.createToken(member.getUsername());

        refreshTokenRepository.findByKey(loginRequestDto.getUsername()).ifPresent(
                newRefreshToken ->
                        refreshTokenRepository.save(newRefreshToken.updateValue(jwtTokenProvider.createToken(username).getRefreshToken())));

        if(refreshTokenRepository.findByKey(loginRequestDto.getUsername()).isEmpty()) {
            RefreshToken refreshToken = RefreshToken.builder()
                    .key(loginRequestDto.getUsername())
                    .value(tokens.getRefreshToken())
                    .build();
            refreshTokenRepository.save(refreshToken);
        }


        return tokens;
    }

    //현재 SecurityContext에 있는 유저 정보 가져오기기
    @Transactional
    public LoginResponseDto getMyInfo() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //유저 정보 있는지 확인
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Security Context에 인증 정보가 없습니다");
        }
        //토큰에서 유저 네임으로 찾은 유저 정보 정보 중 Dto에 해당하는 필드 가져오기 (여기선 유저 네임)
        return userRepository.findByUsername(authentication.getName())
                .map(LoginResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }

    @Transactional
    public TokenDto reissue(TokenDto requestDto) {
        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateRefreshToken(requestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 User ID 가져오기
//        Authentication authentication = jwtTokenProvider.getAuthentication(requestDto.getAccessToken());


        /*
         getUserPk()는 private로 감춰야하지만 authentication의 principle 멤버변수인 userdetailsImpl에 username이 안가져와져서 임시로
         열어두었다.
         */
        String userPk = jwtTokenProvider.getUserPk(requestDto.getAccessToken());


        // 3. 저장소에서 User ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(userPk)
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));
        log.info("Refresh Token의 authentication객체에 저장된 이름="+userPk);

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(requestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.createToken(userPk);
//                TokenDto.builder()
//                .accessToken(jwtTokenProvider.createAccessToken(authentication.getName()))
//                .build();

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }
}