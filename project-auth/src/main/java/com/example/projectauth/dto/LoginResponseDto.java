package com.example.projectauth.dto;


import com.example.projectauth.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private String username;

    //로그인된 유저 정보 가져올 단위
    public static LoginResponseDto of(Member member) {
        return new LoginResponseDto(member.getUsername());
    }

}