package com.example.projectauth.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommonResponse<T> { // HTTP Request 에 대한 응답 데이터를 포함하는 클래스
    private final int code;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T result;


    @Builder
    public CommonResponse(int code, T result) {
        this.code = code;
        this.result = result;
    }

}