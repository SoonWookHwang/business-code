package com.example.projectauth.controller;


import com.example.projectauth.dto.LoginRequestDto;
import com.example.projectauth.dto.SignupRequestDto;
import com.example.projectauth.dto.TokenDto;
import com.example.projectauth.model.Member;
import com.example.projectauth.response.ApiUtils;
import com.example.projectauth.response.CommonResponse;
import com.example.projectauth.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "signup"/*,consumes = {MediaType.APPLICATION_JSON_VALUE/*, MediaType.MULTIPART_FORM_DATA_VALUE}*/)
    public CommonResponse<?> registerMember(@RequestBody @Validated SignupRequestDto requestDto/*,
                                            @RequestPart(required = false) MultipartFile img*/) {
        memberService.registerUser(requestDto/*,img*/);
        return ApiUtils.success(201, "회원가입에 성공하였습니다.");
    }

//    @PostMapping("/login")
//    public CommonResponse<?> login(@RequestBody LoginRequestDto loginRequestDto) {
//        log.info("로그인 컨트롤러 진입");
//        TokenDto token = memberService.login(loginRequestDto);
//        return ApiUtils.success(HttpStatus.OK.value(), token);
//    }
    @PostMapping("/login")
    public CommonResponse<?> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
        TokenDto tokens = memberService.login(requestDto);

        response.setHeader("X-AUTH-TOKEN", tokens.getAccessToken());
        Cookie cookie = new Cookie("X-AUTH-TOKEN", tokens.getAccessToken());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);

        return ApiUtils.success(HttpStatus.OK.value(), tokens);
    }


    @PostMapping("/reissue")
    public CommonResponse<?> reissue(@RequestBody TokenDto requestDto) {
        try {
            return ApiUtils.success(HttpStatus.OK.value(),memberService.reissue(requestDto));
        } catch (Exception e) {
            return ApiUtils.failed(400,e.getMessage());
        }
    }



//    @PostMapping("/update")
//    public CommonResponse<?> update(@AuthenticationPrincipal UserDetailsImpl userDetails,
//                                    @RequestPart UpdateMemberRequestDto requestDto,
//                                    @RequestPart(required = false) MultipartFile image) {
//        String a = "완료";
//        return new CommonResponse<>(200,a);
//
//    }

}
