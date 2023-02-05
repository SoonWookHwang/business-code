package com.example.projectauth.controller;


import com.example.projectauth.dto.LoginRequestDto;
import com.example.projectauth.dto.SignupRequestDto;
import com.example.projectauth.response.ApiUtils;
import com.example.projectauth.response.CommonResponse;
import com.example.projectauth.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {
    private final MemberService memberService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "signup"/*,consumes = {MediaType.APPLICATION_JSON_VALUE/*, MediaType.MULTIPART_FORM_DATA_VALUE}*/)
    public CommonResponse<?> registerMember(@RequestBody @Validated SignupRequestDto requestDto/*,
                                            @RequestPart(required = false) MultipartFile img*/) {
        memberService.registerUser(requestDto/*,img*/);
        return ApiUtils.success(201, "회원가입에 성공하였습니다.");
    }

    @PostMapping("/login")
    public CommonResponse<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        String token = memberService.login(loginRequestDto);
        return ApiUtils.success(200, token);
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
